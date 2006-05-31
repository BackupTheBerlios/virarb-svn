/**
 * House.java: Simple Java3D application, demonstrating the setup of a Java3D application, and the usage
 * of some primitive types.
 */

import java.applet.Applet;
import java.awt.BorderLayout;

//import ShadowApp.SimpleShadow;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Cylinder;
import javax.media.j3d.*;
import javax.vecmath.*;

public class House extends Applet
{

	 private BoundingSphere BSphere = null;
	 private OrbitBehavior  Orbtl   = null;
	 
	public BranchGroup createSceneGraph()
	{
		//blabla
		BranchGroup objRoot = new BranchGroup();
		
		//Skalierung des Spielbereiches
		Transform3D scale = new Transform3D();
		scale.setScale(0.5);
		TransformGroup scaleFinish = new TransformGroup(scale);
		objRoot.addChild(scaleFinish);
		
		//Rotation
		Transform3D rotate = new Transform3D();
		Transform3D tempRotate = new Transform3D();
		rotate.rotX( Math.PI*0.1d);
		tempRotate.rotY( Math.PI / 5.0d );
		rotate.mul( tempRotate );
		TransformGroup objRotate = new TransformGroup( rotate);
		scaleFinish.addChild( objRotate );
		
		//Licht
		SpotLight Licht = new SpotLight();
		Licht.setInfluencingBounds( new BoundingSphere() );
		Licht.setColor( new Color3f( 1.8f, 0.6f, 0.0f ) );
		Vector3f test = new Vector3f( 0.0f, -5.0f, 0.0f );
		Point3f test2 = new Point3f( 0.0f, 0.2f, -0.1f );
		Licht.setDirection( test );
		Licht.setPosition(test2);
		Licht.setSpreadAngle(1.01f);
		Licht.setConcentration(3);
		
		SpotLight Licht2 = new SpotLight();
		Licht2.setInfluencingBounds( new BoundingSphere() );
		Licht2.setColor( new Color3f( 0.0f, 0.0f, 5.0f ) );
		Vector3f test3 = new Vector3f( 2.0f, -5.0f, 1.0f );
		Point3f test4 = new Point3f( 0.0f, 0.2f, -0.1f );
		Licht2.setDirection( test3 );
		Licht2.setPosition(test4);
		Licht2.setSpreadAngle(1.5f);
		Licht2.setConcentration(3);
		
		DirectionalLight Licht_gesamt = new DirectionalLight();
		Licht_gesamt.setInfluencingBounds( new BoundingSphere() );
		Licht_gesamt.setColor( new Color3f( 1.0f, 1.0f, 1.0f ) );
		Vector3f dir_light = new Vector3f( -1.0f, -5.0f, -3.0f );
		dir_light.normalize();
		Licht_gesamt.setDirection( dir_light );
		
		objRoot.addChild(Licht_gesamt);

		// objSpin-Node
		TransformGroup objSpin = new TransformGroup();
		objSpin.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		objRotate.addChild( objSpin );
		
		Background bg = new Background();
		bg.setColor(0.2f,0.2f,0.2f);
		bg.setApplicationBounds( new BoundingSphere() );
		
		TransformGroup tmp = new TransformGroup();	
		Box field_blue;
		Transform3D[] left_blue = new Transform3D[65];
		TransformGroup[] sLeft_blue = new TransformGroup[65];
		float koordX= -0.6f, koordZ=-0.05f, koordY=-0.8f;
		float r=0.95f,g=0.95f,b=0.95f;
		int j=0;
		for (int i=1; i<65; i++){
			if ((i%2!=0 && j%2==0)||(i%2==0 && j%2!=0)){
				r=0.3f; g=0.3f; b=0.3f;
			}
			else{
				r=0.95f;	g=0.95f;	b=0.95f;
			}
		field_blue  = new Box( 0.1f, 0.02f, 0.1f, Box.GENERATE_NORMALS, createAppearance(r, g, b, 50.0f) );
		left_blue[i] = new Transform3D();
		left_blue[i].setTranslation( new Vector3f( koordX, koordZ, koordY  ) );
		sLeft_blue[i] = new TransformGroup( left_blue[i] );
		sLeft_blue[i].addChild( field_blue );
		tmp.addChild( sLeft_blue[i] );
		if (koordX<0.7f){
		koordX += 0.2f;
			}
		else{
			koordX = -0.6f;
			koordY += 0.2f;
			j++;
			}
		} 
		objSpin.addChild(tmp);

		//Cylinder (Breite, Höhe)
		//Vector (links/rechts,Höhe,Vorne/hinten) und verschiebung nach oben
		Cylinder stein = new Cylinder( 0.1f, 0.05f, Cylinder.GENERATE_NORMALS, createAppearance(0.8f, 0.2f, 0.1f, 20.0f) );
		Transform3D hoch = new Transform3D();
		hoch.setTranslation( new Vector3f( 0.0f, -0.2f, 0.0f ) );
		TransformGroup hoch1 = new TransformGroup( hoch );
		hoch1.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		hoch1.addChild( stein );
		hoch1.addChild(Licht);
		objSpin.addChild( hoch1 );
		//GeometryArray tester;
		//SimpleShadow schatten = new SimpleShadow(tester, new Vector3f( 0.0f, 0.1f, 0.0f ) , new Color3f(1.0f, 1.0f, 1.0f), 0.5);
	
		Appearance appear=new Appearance();
		Material mat = new Material();
		mat.setShininess( 30f );
		mat.setDiffuseColor( new Color3f(0.2f, 0.2f, 0.2f) );
		mat.setAmbientColor( new Color3f(0.128f, 0.128f, 0.128f) );
		mat.setSpecularColor( new Color3f(0.230f, 0.230f, 0.230f) );
        appear.setMaterial(mat);
        stein.setAppearance(appear);
        
		//Figur 2
		Cylinder figur2 = new Cylinder( 0.1f, 0.05f, Cylinder.GENERATE_NORMALS, createAppearance(0.1f, 0.5f, 0.1f, 20.0f) );
		Transform3D hoch2 = new Transform3D();
		hoch2.setTranslation( new Vector3f( 0.4f, 0.0f, 0.4f ) );
		TransformGroup hoch12 = new TransformGroup( hoch2 );
		hoch12.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		hoch12.addChild( figur2 );
		hoch12.addChild(Licht2);
		objSpin.addChild( hoch12 );
		
		Vector3f direction = new Vector3f(-1.0f, -1.0f, -1.0f);
	    direction.normalize();
	    
		/*Shape3D shadow = new SimpleShadow(GeometryObject.
                direction,
                new Color3f( 0.2f, 0.2f,  0.2f),
                -0.5f);
*/
		//Figur 3
		Cylinder figur3 = new Cylinder( 0.1f, 0.05f, Cylinder.GENERATE_NORMALS, createAppearance(0.1f, 0.5f, 0.1f, 20.0f) );
		Transform3D hoch3 = new Transform3D();
		hoch3.setTranslation( new Vector3f( 0.8f, 0.00f, -0.2f ) );
		TransformGroup hoch13 = new TransformGroup( hoch3 );
		hoch13.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		hoch13.addChild( figur3 );
		objSpin.addChild( hoch13 );
        
		BoundingSphere bounds = new BoundingSphere();
		
		
		 float[] knots = new float[3];
	        for (int n = 0; n < 3; ++n) {
	            knots[n] = (float)n * (1 / (float)(3-1));
	        }
		
		   
	        //PositionInterpolator position = new PositionInterpolator(movement, hoch1, xzAxis, 0.3f, -0.3f);
	        Transform3D axe = new Transform3D();
	        //axe.rotZ(Math.PI/2);
	        PositionPathInterpolator springen =
	            new PositionPathInterpolator(
	                new Alpha(-1,Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,0,0,2500,1000,2000,4000,1000,1000),
	                hoch13,
	                axe,
	                knots,
	                createCircleCoords(0.2f, 3)
	            );
	      	springen.setSchedulingBounds(bounds);
	        hoch13.addChild(springen);
	        
	        /*TransformGroup objMonth = new TransformGroup();
	        objMonth.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	        RotationInterpolator rotIntMonth =
	            new RotationInterpolator(new Alpha(-1, 3000), objMonth);
	        rotIntMonth.setSchedulingBounds(bounds);
	        
	        
	        objYear.addChild(objMonth);*/
	        
		/*Transform3D yAxis = new Transform3D();
		yAxis.rotZ((Math.PI/2+50));
		Alpha rotationAlpha = new Alpha( -1, 10000 );
		
		RotationInterpolator rotator = new RotationInterpolator( 
		rotationAlpha, hoch13, yAxis, 0.0f, (float)Math.PI * 2.0f );
		hoch13.addChild(rotator);
		
		rotator.setSchedulingBounds(bounds);*/
		
		
		Transform3D xzAxis = new Transform3D();
		//Alpha movement = new Alpha(-1,4000);
		Alpha movement = new Alpha(-1,Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,0,0,2500,1000,2000,4000,1000,1000);
		xzAxis.rotY((Math.PI/4));
		PositionInterpolator position = new PositionInterpolator(movement, hoch1, xzAxis, 0.3f, -0.3f);
		System.out.println("Starposition: "+position.getStartPosition());
		System.out.println("Endposition: "+position.getEndPosition());
		hoch1.addChild(position);
		position.setSchedulingBounds(bounds);
		
		Alpha moveAlpha = new Alpha(-1,Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,0,0,2500,1000,2000,4000,1000,1000);
		Transform3D val = new Transform3D();
		val.rotX((Math.PI/4));
		PositionInterpolator position2 = new PositionInterpolator(moveAlpha, hoch12, val, 0.4f, 0.8f);
		hoch12.addChild(position2);
		position2.setSchedulingBounds(bounds);
		
		Background backg = new Background(0.5f,0.5f,0.5f);
		backg.setApplicationBounds(bounds);
		objRoot.addChild(backg);

		// finally, compile the finnished tree (Java3D performs optimizations here)
		objRoot.compile();

		return objRoot;
	}
	
	private Point3f[] createCircleCoords(float r, int N) {
        Point3f[] coords = new Point3f[N];
        int       n;
        double    a;
        float     x, z;

       /* for (a = 0, n = 0; n < N; a = 1.0 * Math.PI / (N-1) * ++n) {
            x = (float) (r * Math.sin(a));
            z = (float) (r * Math.cos(a));         
            coords[n] = new Point3f(x, 0f, z);
            System.out.println(coords[n]);
        }*/
        
        
        coords[0]= new Point3f(-0.6f,0.0f,0.6f);
        coords[1]= new Point3f(0.0f,0.3f,0.0f);
        coords[2]= new Point3f(0.8f,0.0f,-0.8f);
        
        
        
        return coords;
    } 
	

	public House()
	{
		
		 BSphere = new BoundingSphere(new Point3d(0f, 0f, 0f), 100f);
		// setup the window layout, create the Canvas3D object and add it to the window
		setLayout( new BorderLayout() );
		Canvas3D canvas = new Canvas3D( SimpleUniverse.getPreferredConfiguration() );
		add( "Center", canvas );

		// create the scenegraph
		BranchGroup scene = createSceneGraph();

		// create the universe for our world
		SimpleUniverse universe = new SimpleUniverse( canvas );

		 ViewingPlatform view = universe.getViewingPlatform();
		// set the viewing platform (i.e. the "view" side of the scene graph)
		 view.setNominalViewingTransform();

		// finally, add the scenegraph to the universe
		universe.addBranchGraph( scene );
		Orbtl = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ALL | OrbitBehavior.STOP_ZOOM);
	    Orbtl.setSchedulingBounds(BSphere);
	    view.setViewPlatformBehavior(Orbtl);
	    
	    Transform3D l3D = new Transform3D();
	    l3D.lookAt(new Point3d(0.8,1.4,4), 
	               new Point3d(0,0,0), 
	               new Vector3d(0,1,0));
	    l3D.invert();
	    universe.getViewingPlatform().
	            getViewPlatformTransform().
	            setTransform(l3D);
	}

	public static void main( String[] argv )
	{
		// simply create a new instance of the window, here
		new MainFrame( new House(), 512, 512 );
	}


	private Appearance createAppearance( float r, float g, float b, float s )
	{
	Appearance app = new Appearance();
	Material mat = new Material();
	mat.setShininess( s );
	mat.setDiffuseColor( new Color3f(r, g, b) );
	app.setMaterial( mat );
	return( app );
	}
}