
import java.applet.Applet;
import java.awt.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Cylinder;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Spielfeld extends Applet
{
	/**
	 * Creates a new scenegraph.
	 * @return the new scenegraph.
	 */
	public BranchGroup createSceneGraph()
	{
		// the root node of the tree
		BranchGroup objRoot = new BranchGroup();

		Transform3D rotate = new Transform3D();
		Transform3D tempRotate = new Transform3D();

		// setup rotations for the x and y direction, and multiply them together
		rotate.rotX( Math.PI * 0.1d );
		tempRotate.rotY( Math.PI / 5.0d );
		rotate.mul( tempRotate );

		// apply that rotation to the whole scene that will follow
		TransformGroup objRotate = new TransformGroup( rotate );
		objRoot.addChild( objRotate );

		// also create a transform group for the spinning of the scene
		TransformGroup objSpin = new TransformGroup();
		objSpin.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		objRotate.addChild( objSpin );



		// create the box for the field

		Box[][] felder=new Box[8][8];
		float sx=-0.35f;
		float sz=-0.35f;
		for(int i=0;i<8;i++){		
			for(int j=0;j<8;j++){		
				if((i%2+j)%2==0){
					felder[i][j] = new Box( 0.05f, 0.005f, 0.05f, Box.GENERATE_NORMALS, createAppearance(0.15f, 0.15f, 0.15f, 200.0f) );
				}
				else{
					felder[i][j] = new Box( 0.05f, 0.005f, 0.05f, Box.GENERATE_NORMALS, createAppearance(0.850f,0.85f,0.85f, 200.0f) );				
				}
				
				Transform3D x=new Transform3D();			
				x.setTranslation( new Vector3f(sx,0.0f,sz) );
				sx+=0.10f;
						
				TransformGroup pos = new TransformGroup( x );
				pos.addChild(felder[i][j]);			
				
				objSpin.addChild(pos);	
			}
			sx=-0.35f;
			sz+=0.1f;
		}
		
	
		//create red Cylinders for player1
		Cylinder[] figurewhite=new Cylinder[12];
		sx=-0.35f;
		sz=-0.35f;
		
		for(int i=0;i<3;i++){
			for(int j=0;j<4;j++){
				Transform3D x=new Transform3D();

				figurewhite[i*4+j] = new Cylinder( 0.04f, 0.01f, Cylinder.GENERATE_NORMALS, createAppearance(0.8f, 0.8f, 0.8f, 20.0f) );
				
				x.setTranslation( new Vector3f( sx, 0.01f, sz ) );
				TransformGroup pos = new TransformGroup( x );
				
				if(i==2 && j==0){
					Transform3D x2=new Transform3D();
					TransformGroup pos2 = new TransformGroup(x2);
					pos2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

					Transform3D richtung = new Transform3D();
					richtung.rotY(-Math.PI/4);
					Alpha positionAlpha = new Alpha(-1,Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,0,0,2500,1000,2000,4000,1000,1000);
//					positionAlpha.setPhaseDelayDuration(2000);
					PositionInterpolator positioner = new PositionInterpolator(positionAlpha, pos2, richtung, 0.0f, 0.284f);
					positioner.setSchedulingBounds(new BoundingSphere());				 
					figurewhite[i*4+j].addChild(positioner);				
					pos2.addChild(figurewhite[i*4+j]);
					
					SpotLight spotLight = new SpotLight();
					spotLight.setInfluencingBounds( new BoundingSphere() );
					spotLight.setColor( new Color3f( 0.8f, 0.6f, 0.0f ) );
					Vector3f test = new Vector3f( 0.0f, -5.0f, 0.0f );
					Point3f test2 = new Point3f( 0.0f, 0.2f, -0.1f );
					spotLight.setDirection( test );
					spotLight.setPosition(test2);
					spotLight.setSpreadAngle(1.01f);
					spotLight.setConcentration(3);

					pos2.addChild(spotLight);
			
					
					pos.addChild(pos2);
				}
				else{
					pos.addChild( figurewhite[i*4+j]);
				}
				objSpin.addChild( pos );
				sx+=0.2f;
			}
			sx=-0.35f+((i+1)%2)*0.1f;
			sz+=0.1f;
		}
		//create white Cylinders for player2
		Cylinder[] figureblack=new Cylinder[12];
//		TransformGroup[] tgb=new TransformGroup[12];
		sx=0.35f;
		sz=0.35f;
	
		for(int i=0;i<3;i++){
			for(int j=0;j<4;j++){
				figureblack[i*4+j] = new Cylinder( 0.04f, 0.01f, Cylinder.GENERATE_NORMALS, createAppearance(0.5f, 0.0f, 0.0f, 200.0f) );
				
				Transform3D x=new Transform3D();
				x.setTranslation( new Vector3f( sx, 0.01f, sz ) );
				
				TransformGroup pos = new TransformGroup( x );
				if(i==2 && j==3){
					Transform3D x2=new Transform3D();
					TransformGroup pos2 = new TransformGroup(x2);
					pos2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

					Transform3D richtung = new Transform3D();
					richtung.rotY(Math.PI/4);
					
					
//					Alpha positionAlpha = new Alpha(10, 5000);
//					positionAlpha.setPhaseDelayDuration(2000);
					
//					PositionInterpolator positioner = new PositionInterpolator(positionAlpha, pos2, richtung, 0.0f, 0.5f);
//					positioner.setSchedulingBounds(new BoundingSphere());	
					
					

					
					   
			        //PositionInterpolator position = new PositionInterpolator(movement, hoch1, xzAxis, 0.3f, -0.3f);
			        //axe.rotZ(Math.PI/2);
					float[] knots = {0.0f,0.5f,1.0f};
					PositionPathInterpolator positioner =
			            new PositionPathInterpolator(
			                new Alpha(-1,Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,0,0,2500,1000,2000,4000,1000,1000),
			                pos2,
			                richtung,
			                knots,
			                createCoords()
			         );
					
					
			        positioner.setSchedulingBounds(new BoundingSphere());
					figureblack[i*4+j].addChild(positioner);				
					pos2.addChild(figureblack[i*4+j]);
					
					SpotLight spotLight = new SpotLight();
					spotLight.setInfluencingBounds( new BoundingSphere() );
					spotLight.setColor( new Color3f( 0.5f, 0.0f, 0.0f ) );
					Vector3f test = new Vector3f( 0.0f, -5.0f, 0.0f );
					Point3f test2 = new Point3f( 0.0f, 0.2f, -0.1f );
					spotLight.setDirection( test );
					spotLight.setPosition(test2);
					spotLight.setSpreadAngle(1.01f);
					spotLight.setConcentration(3);

					pos2.addChild(spotLight);
			
					
					pos.addChild(pos2);
				}
				else
				{	
					pos.addChild( figureblack[i*4+j] );
				}
				objSpin.addChild( pos );
				sx-=0.2f;
			}
			sx=0.35f-((i+1)%2)*0.1f;
			sz-=0.1f;
		}

//		Transform3D x=new Transform3D();
//		TransformGroup temp=(TransformGroup)objSpin.getChild(64);
//		temp.getTransform(x);
//		Vector3f v=new Vector3f();
//		x.get(v);
//		x.setTranslation(new Vector3f(v.x,v.y+0.5f,v.z));	
//		temp.setTransform(x);

		// setup a directional light source
		DirectionalLight dLight = new DirectionalLight();
		dLight.setInfluencingBounds( new BoundingSphere() );
		dLight.setColor( new Color3f( 1.0f, 1.0f, 1.0f ) );
		Vector3f dir_light = new Vector3f(0.7f, -1.0f, 0.5f );
		dir_light.normalize();
		dLight.setDirection( dir_light );
		objSpin.addChild( dLight );

		
		// setup the continuous rotation of the scene around the y-axis
//		Transform3D yAxis = new Transform3D();
//		Alpha rotationAlpha = new Alpha( -1, 10000 );
//		RotationInterpolator rotator = new RotationInterpolator( rotationAlpha, objSpin, yAxis, 0.0f, (float)Math.PI * 2.0f );

//		set the bounding sphere of the scene
//		BoundingSphere bounds = new BoundingSphere();
//		rotator.setSchedulingBounds( bounds );
//		objSpin.addChild( rotator );

		// finally, compile the finnished tree (Java3D performs optimizations here)
		objRoot.compile();

		return objRoot;
	}

	public Spielfeld()
	{
		// setup the window layout, create the Canvas3D object and add it to the window
		setLayout( new BorderLayout() );
		Canvas3D canvas = new Canvas3D( SimpleUniverse.getPreferredConfiguration() );
//		setBackground(Color.WHITE);
		add( "Center", canvas );

		// create the scenegraph
		BranchGroup scene = createSceneGraph();

		// create the universe for our world
		SimpleUniverse universe = new SimpleUniverse( canvas );

		// set the viewing platform (i.e. the "view" side of the scene graph)
		universe.getViewingPlatform().setNominalViewingTransform();

		
//		 finally, add the scenegraph to the universe
		universe.addBranchGraph(scene);

		// Mausbewegung
		ViewingPlatform viewingPlatform = universe.getViewingPlatform();
		viewingPlatform.setNominalViewingTransform();

		OrbitBehavior orbit = new OrbitBehavior(canvas,
				OrbitBehavior.REVERSE_ALL);
		orbit.setSchedulingBounds(new BoundingSphere(new Point3d(), 100));
		viewingPlatform.setViewPlatformBehavior(orbit);
	}

	public static void main( String[] argv )
	{
		// simply create a new instance of the window, here
		new MainFrame( new Spielfeld(),512, 512 );
	}

	/**
	 * Creates a simple color and shading-only appearance object using the given color.
	 * @param r the red value of the desired object color
	 * @param g the green value of the desired object color
	 * @param b the blue value of the desired object color
	 * @param s the shininess of the object
	 * @return the created Appearance object
	 */
	private Appearance createAppearance( float r, float g, float b, float s )
	{
		Appearance app = new Appearance();

		Material mat = new Material();
		mat.setShininess( s );
		mat.setDiffuseColor( new Color3f(r, g, b) );
//		mat.setSpecularColor( new Color3f(1.0f, 1.0f, 1.0f) );
//		mat.setAmbientColor( new Color3f(1.0f, 1.0f, 1.0f) );
		app.setMaterial( mat );
		return( app );
	}
	
	private Point3f[] createCoords() {
        Point3f[] coords = new Point3f[3];
   
        coords[0]= new Point3f(0.0f,0.0f,0.0f);
        coords[1]= new Point3f(0.15f,0.1f,0.0f);
        coords[2]= new Point3f(0.284f,0.0f,0.0f);
        return coords;
    } 
	
}


