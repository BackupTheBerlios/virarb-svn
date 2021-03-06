package gnu.cajo.utils.extra;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import gnu.cajo.invoke.Remote;

/*
 * cajo object oriented rule engine
 * Copyright (C) 2006 John Catherino
 * The cajo project: https://cajo.dev.java.net
 *
 * For issues or suggestions mailto:cajo@dev.java.net
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, at version 2.1 of the license, or any
 * later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You can receive a copy of the GNU Lesser General Public License from their
 * website, http://fsf.org/licenses/lgpl.html; or via snail mail, Free
 * Software Foundation Inc., 51 Franklin Street, Boston MA 02111-1301, USA
 */

/**
 * This class is a cajo-based reformulation of rule-based reasoning systems,
 * for declarative programming. This engine does not suffer from the large
 * memory requirements associated with the Rete Algorithm, as it offloads
 * each rule's state and inference to distributed JVMs. The engine is also
 * serialisable; to allow it to scale, as well as providing both modularity,
 * and redundancy.<p>
 * A rule engine can be likened to a database, with some important
 * differences. In database parlance, what is known as the schema is called
 * an ontology in a rule engine. Unlike a database, multiple facts are
 * deposited in any given slot, a database is somewhat less dynamic in its
 * change of existing data. Data in a database are called facts in a Rule
 * engine. A database supports queries on its stored data, this is not
 * possible for the rule engine, as it sends its facts to registered
 * listeners. Unlike the Java language, an ontology logically, and therefor
 * must, support multiple inheretance.<p>
 * Since only the RuleEngine ontology is stored at the server host, and
 * not the fact base; this makes the creation of extremely large distributed
 * rule bases both possible, and very simple. The applciation specific
 * knowledge bases are offloaded to the clients as well.<p>
 * The application of both fuzzy facts, and fuzzy predicates is not only
 * easily supported, but is in fact very <i>highly</i> recommended.<p>
 * <i><u>Note</u>:</i> to compile this class, it is best to set the java
 * compiler's <tt>-source</tt> switch to <tt>1.2</tt>.
 *
 * @version 1.0, 11-May-06
 * @author John Catherino
 */
public class RuleEngine implements java.io.Serializable {
   /**
    * This is a good base class for building rules. It minimises load
    * on the rule engine, and properly manages asynchronous change without
    * synchronisation, or deadlock problems. It is highly recommended to
    * extend this class for your rules. The collection of registered rules
    * constitute the <i>knowledge base</i> of the rule engine.
    */
   public static class Rule implements java.io.Serializable {
      /**
       * The rule specific local fact base on which to infer. It allows
       * a rule to encompass many different aspects of an ontology.
       */
      protected Hashtable facts = new Hashtable();
      /**
       * This flag indicates that asynchronous modification has been
       * made to the fact base. <i><u>Very Important</u>:</i>
       * The <i>first</i> instruction of the infer method should be to
       * set it false.
       */
      protected boolean change;
      /**
       * The local reasoning thread. It is lazily instantiated, and can
       * be shut down by calling its interrupt method, if necessary.
       * It's job is to call the infer method whenever there is a
       * change (addidion, removal, modification) to the local fact
       * base.
       */
      protected transient Thread thread;
      /**
       * This method is completely rule-specific. It is called when there
       * has been a change to the local fact base. It should first
       * reset the change flag, which is done simply by calling super(),
       * then evaluate its state to make decisions. It is possible for the
       * fact base to be actively changing, while this method is executing.
       * This can be detected by monitoring the state of the change flag.
       */
      protected void infer() { change = false; }
      /**
       * This method extracts the Hashtable corresponding to the provided
       * key series. If a path to the Hashtable does not exist, it will be
       * created automatically. It is called by the posit method, but it
       * is also a handy utility for the infer method, to check the status
       * of interesting data elements.
       * @param keys The ordered sequence of candidate keys leading to the
       * fact storage position in the fact base.
       * @return The hashtable indexed one key before the final in the
       * array. The fact of interest is located in this table, at the
       * final key value.
       */
      protected synchronized Hashtable select(Object keys[]) {
         Hashtable element = (Hashtable)facts.get(keys[0]);
         for (int i = 1; i < keys.length - 1; i++) {
            Object temp = element.get(keys[i]);
            if (temp == null)  { // if no path, make one!
               temp = new Hashtable();
               element.put(keys[i], temp);
            }
            element = (Hashtable)temp;
         }
         return element;
      }
      /**
       * This method is invoked by the rule engine to make a change,
       * addition or modification, to the local fact base. It will
       * notify the local processing thread, creating it if necessary, and
       * return. Its run time is designed to be as short as possible, and
       * should not be overridden, unless it is absolutely critical.
       * @param fact A datum of interest, which has been changed at the
       * rule engine.
       * @param keys The ordered sequence of candidate keys leading to the
       * fact storage position in the fact base.
       */
      public synchronized void posit(Object fact, Object keys[]) {
         select(keys).put(keys[keys.length - 1], fact);
         change = true;
         if (thread == null) {
            thread = new Thread(new Runnable() {
               public void run() {
                  try {
                     while (true) {
                        infer();
                        synchronized(Rule.this) {
                           while(!change) Rule.this.wait();
                        }
                     }
                  } catch(InterruptedException x) {}
               }
            });
            thread.start();
         } else notify();
      }
   }
   /**
    * This field represents the set of listening rules registered in the
    * engine's ontology. The element stored under each key can be either a
    * Vector, indicating a collection of listening rules, or another
    * Hashtable, indicating an additional dimension to the knowledge base.
    * This allows for an n-ary dimensional organisation of rules references,
    * structured about keys. Rules are generally assumed to be references to
    * objects in remote JVMs, but that can be local object references, if
    * necessary. Multiple rules can be assigned to the same dimension, just
    * as the same rule can be assigned to multiple dimension.
    */
   protected final Hashtable rules = new Hashtable();
   /**
    * This method is called by the public assert method. It will
    * recursively traverse through the ontology, invoking one or more
    * assertions based on the key sequence provided.<p>
    * @param rules The ontology on which to operate.
    * @param keys The chain of keys leading to the element(s) of interest
    * in the ontology. If any key value is null, it is considered a wild
    * card, resulting in all of the keys at that dimension being used.
    * @param offset The index into the provided key sequence.
    * @param data A two element array containing the fact and the key
    * sequence array. It will be provided to each listening rule as the
    * arguments to its posit method.
    */
   protected static void posit(
      Hashtable rules, Object keys[], int offset, Object data) {
      try {
         if (keys[offset] != null) { // fixed key, linear recursion
            if (offset == keys.length - 1) { // boundary condition
               Object listeners[] =
                  ((Vector)rules.get(keys[offset])).toArray();
               for (int i = 0; i < listeners.length; i++) try {
                  Remote.invoke(listeners[i], "posit", data);
               } catch(Exception x) {
                  ((Vector)rules.get(listeners[i])).remove(listeners[i]);
               }
            } else posit((Hashtable)rules.get(keys[offset]),
               keys, offset + 1, data); // continue recursing
         } else {  // wildcard, n-ary recursion
            Enumeration wildKeys = rules.keys();
            while(wildKeys.hasMoreElements())
               posit((Hashtable)rules.get(wildKeys.nextElement()),
                  keys, offset + 1, data);
         }
      } catch(ClassCastException x) {} // invalid key path, ignore
   }
   /**
    * This method is called by the public add method. It will
    * recursively traverse through the ontology, invoking one or more
    * rule locations based on the key sequence provided.<p>
    * @param rules The ontology on which to operate.
    * @param keys The chain of keys leading to the element(s) of interest
    * in the ontology. If any key value is null, it is considered a wild
    * card, resulting in all of the keys at that dimension being used.
    * @param offset The index into the provided key sequence.
    */
   protected static void add(
      Hashtable rules, Object rule, Object keys[], int offset) {
      try {
         if (keys[offset] != null) { // fixed key, linear recursion
            synchronized(rules) { // get next path node
               Hashtable table = (Hashtable)rules.get(keys[offset]);
               if (table == null) { // if no node, make one!
                  table = new Hashtable();
                  rules.put(keys[offset], table);
               }
            }
            rules = (Hashtable)rules.get(keys[offset]);
            if (offset == keys.length - 1) { // boundary condition
               Vector vector = (Vector)rules.get(keys[keys.length - 1]);
               if (vector == null) {
                  vector = new Vector();
                  rules.put(keys[keys.length - 1], vector);
               }
               vector.add(rule);
            } else add(rules, rule, keys, offset + 1); // continue recursing
         } else {  // wildcard, n-ary recursion
            Enumeration wildKeys = rules.keys();
            while(wildKeys.hasMoreElements())
               add((Hashtable)rules.get(wildKeys.nextElement()),
                  rule, keys, offset + 1);
         }
      } catch(ClassCastException x) {} // invalid key path, ignore
   }
   /**
    * This method is called by the public remove method. It will
    * recursively traverse through the ontology, removing one or more
    * rule locations based on the key sequence provided.<p>
    * @param rules The ontology on which to operate.
    * @param keys The chain of keys leading to the element(s) of interest
    * in the ontology. If any key value is null, it is considered a wild
    * card, resulting in all of the keys at that dimension being used.
    * @param offset The index into the provided key sequence.
    */
   protected static void remove(
      Hashtable rules, Object rule, Object keys[], int offset) {
      try {
         if (keys[offset] != null) { // fixed key, linear recursion
            rules = (Hashtable)rules.get(keys[offset]);
            if (offset == keys.length - 1) { // boundary condition
               Vector vector = (Vector)rules.get(keys[keys.length - 1]);
               vector.remove(rule);
            } else remove(rules, rule, keys, offset + 1); // continue
         } else {  // wildcard, n-ary recursion
            Enumeration wildKeys = rules.keys();
            while(wildKeys.hasMoreElements())
               remove((Hashtable)rules.get(wildKeys.nextElement()),
                  rule, keys, offset + 1);
         }
      } catch(ClassCastException x) {} // invalid key path, ignore
   }
   /**
    * This constructor performs no function, as the operation of the rule
    * engine is completely runtime dependent.
    */
   public RuleEngine() {}
   /**
    * This method is used to assert, or modify, a fact. The engine will
    * search its ontology for suitable listeners, and notify them.
    * Each rule will be notified serially, by invoking its public
    * <tt>posit</tt> method, with the fact object, and the collection of
    * keys. If a rule instance determines all of its criteria necessary for
    * firing has been met, then it is the responsibility of that rule to
    * carry it out. It simply invokes the static posit method to begin
    * a recursive ontology traversal.<p>
    * <i><u>Note</u>:</i> Rule <tt>posit</tt> invocations are expected to
    * return as quickly as possible to ensure maximum performance of the
    * engine. Typically rule objects will save the fact argument in a queue,
    * notify a waiting local thread, and return. A rule generally will
    * register for multiple fact types, therefore its posit method most
    * likely <i>will</i> be invoked reentrantly. Also, if the invocation
    * of the rule results in an Exception, it will be removed from the
    * collection of listeners for that element.
    * @param fact The fact that is being asserted or changed.
    * @param keys The chain of keys leading to the element of interest
    * in the ontology. If the key sequence does not exist in the ontology,
    * the invocation will be ignored silently.
    */
   public void posit(Object fact, Object keys[]) {
      posit(rules, keys, 0, new Object[] { fact, keys });
   }
   /**
    * This method is used to register a rule instance with the ontology.
    * The rule engine will store the rule reference, to have its
    * <tt>posit</tt> method invoked, as the state of the fact of interest
    * changes. It is worth mentioning, a single rule instance is often used
    * to monitor multiple types of facts. It simply invokes the static
    * add method, to begin the recursive ontology traversal to add the rule.
    * @param rule The rule to be notified when the fact state changes. It is
    * typically a reference to an object in a remote JVM, but it can be to
    * a local object, or even a proxy object.
    * @param keys The chain of keys leading to the element of interest
    * in the fact base. If the path does not exist, it will be created
    * implicitly.
    */
   public void add(Object rule, Object keys[]) {
      add(rules, rule, keys, 0);
   }
   /**
    * This method is used to unregister a rule instance for a type of 
    * fact. The rule engine will delete the rule reference from its
    * ontology. It simply invokes the static remove method, to begin the
    * recursive ontology traversal to delete the rule.
    * @param rule The rule to be removed from the engine. It is typically a
    * reference to an object in a remote JVM, but it can be to a local
    * object, or even a proxy object.
    * @param keys The chain of keys leading to the element of interest
    * in the ontology. If the key sequence does not exist in the ontology,
    * the invocation will be ignored silently.
    */
   public void remove(Object rule, Object keys[]) {
      remove(rules, rule, keys, 0);
   }
}
