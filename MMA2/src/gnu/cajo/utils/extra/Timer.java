package gnu.cajo.utils.extra;

import java.util.ArrayList;
import gnu.cajo.invoke.Remote;

/*
 * General Purpose Task Synchronizer
 * Copyright (c) 1997 John Catherino
 *
 * For issues or suggestions mailto:cajo@dev.java.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, at version 2.1 of the license, or any
 * later version.  The license differs from the GNU General Public License
 * (GPL) to allow this library to be used in proprietary applications. The
 * standard GPL would forbid this.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * To receive a copy of the GNU Lesser General Public License visit their
 * website at http://www.fsf.org/licenses/lgpl.html or via snail mail at Free
 * Software Foundation Inc., 59 Temple Place Suite 330, Boston MA 02111-1307
 * USA
 */

/**
 * This class supports the timed execution of scheduled tasks. The class makes
 * use of the Scheduler class, in accomplishing its functionality.  This means
 * that shared objects between regular Scheduled tasks, and these timed tasks,
 * will not run into concurrency issues. The object accepts tasks which
 * implement the void slice() method, using the semantics defined in the
 * Scheduler class. Additionally an interval is provided, in milliseconds, for
 * approximate inter-execution delay.  A count can also be provided, to allow
 * the task to be run a fixed number of times.  When a count of zero is
 * provided, the task will run forever, or until removed.  Delay intervals are
 * generally expected to be several seconds, or longer.  Delays much less than
 * one second, may not be scheduled accurately.
 * <p>
 * The class methods are properly synchronized to safely allow multi-thread,
 * and remote access.
 * <p>
 * <i>Note:</i> as with the Scheduler class, this class also supports
 * serialization. Therefore, in order for the serialization to succeed,
 * each of the loaded tasks must also support serialization.
 *
 * @version 1.0, 16-Dec-97 Initial release
 * @author John Catherino
 *
 */

public final class Timer implements java.io.Serializable {
   private final Scheduler sched;
   private final Integer handle;
   private boolean running;
   private ArrayList tasks;
   private final class TimedTask implements java.io.Serializable {
      private final Object task;
      private final long interval;
      private int count;
      private long time;
      private TimedTask(Object task, Long interval, Integer count) {
         this.task     = task;
         this.interval = interval.longValue();
         this.count    = count.intValue();
         this.time     = System.currentTimeMillis() + this.interval;
      }
      private void drop() {
         tasks.remove(tasks.indexOf((this)));
         if (tasks.isEmpty()) {
            tasks = null;
            if (running) {
               running = false;
               sched.stop(handle);
            }
         }
      }
   }
   /**
    * The constructor loads the timer into the Scheduler, but does not yet
    * start it running.  It will start itself automatically, when a task is
    * loaded, and will stop itself similarly, when it has no tasks to run.
    * @param sched The scheduler to use for execution.
    */
   public Timer(Scheduler sched) {
      this.sched = sched;
      handle = sched.load(this);
   }
   /**
    * This method loads a task for timed execution. If the timer object is not
    * currently running in its Scheduler, it will be started automatically.
    * Naturally, the task object must implement a public void slice() method.
    * <i>Note:</i> the task will delay a full interval before its first
    * execution. If an ititial execution is required, it must be done manually.
    * @param task The operation to be timed, it can be local, remote, or even
    * a proxy, when enabled.
    * @param interval The time between execution, in milliseconds
    * @param count The number of times to run before removal, a count of zero
    * indicates to run indefinitely, or until removed
    */
   public synchronized void load(Object task, Long interval, Integer count) {
      if (tasks == null) tasks = new ArrayList();
      tasks.add(new TimedTask(task, interval, count));
      if (!running) {
         running = true;
         sched.wake(handle);
      }
   }
   /**
    * This method prematurely removes a task from the timed execution queue. If
    * the timed task queue becomes empty as a result, the timer task will be
    * stopped in its Scheduler object. If multiple instances of the same task
    * have been loaded, the first encountered in the queue will be removed.
    * @param task The task to discard
    */
   public synchronized void remove(Object task) {
      for (int i = 0; i < tasks.size(); i++) {
         TimedTask tt = (TimedTask)tasks.get(i);
         if (tt.task.equals(task)) {
            tt.drop();
            break;
         }
      }
   }
   /**
    * The timer task implementation itself, invoked by the scheduler.  This
    * method will scan the scheduled tasks for timeout, indicating readiness
    * for execution.  Additionally it will decrement the task's count, when
    * applicable, and remove the task when the count reaches zero.  When the
    * task list becomes empty, it will suspend itself as a scheduled task,
    * until a new task is loaded. If several tasks are eligible for execution
    * in any given slice, only one eligible task will be run, to improve
    * scheduler responsiveness. If the execution of the task slice results in
    * an exception, the task will be automatically dropped from the queue.
    */
   public void slice() {
      TimedTask tt = null;
      synchronized(this) {
         long time = System.currentTimeMillis();
         for (int i = 0; i < tasks.size(); i++) {
            TimedTask task = (TimedTask)tasks.get(i);
            if (time >= task.time) {
               if (task.count != 0 && --task.count == 0) task.drop();
               else task.time += task.interval;
               tt = task;
               break;
            }
         }
      }
      if (tt != null) try { Remote.invoke(tt.task, "slice", null); }
      catch(Exception x)  { synchronized(this) { tt.drop(); } }
   }
}
