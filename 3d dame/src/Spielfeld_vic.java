/**
 * House.java: Simple Java3D application, demonstrating the setup of a Java3D application, and the usage
 * of some primitive types.
 */

import java.applet.Applet;
import java.awt.BorderLayout;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Cylinder;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Spielfeld_vic extends Applet {
	/**
	 * Creates a new scenegraph.
	 * 
	 * @return the new scenegraph.
	 */
	public BranchGroup createSceneGraph() {
		// the root node of the tree
		BranchGroup objRoot = new BranchGroup();

		Transform3D rotate = new Transform3D();
		Transform3D tempRotate = new Transform3D();

		// setup rotations for the x and y direction, and multiply them together
		rotate.rotX(Math.PI * 0.1d);
		tempRotate.rotY(Math.PI / 5.0d);
		rotate.mul(tempRotate);

		// apply that rotation to the whole scene that will follow
		TransformGroup objRotate = new TransformGroup(rotate);
		objRoot.addChild(objRotate);

		// also create a transform group for the spinning of the scene
		TransformGroup objSpin = new TransformGroup();
		objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objRotate.addChild(objSpin);

		// schachfeld erstellen
		// Box schachfeld = new Box(0.6f, 0.05f, 0.6f, Box.GENERATE_NORMALS,
		// createAppearance(0.2f, 0.2f, 0.2f, 50));
		// objSpin.addChild(schachfeld);

		// Schachbrett erstellen

		int x = 8;
		int y = 8;
		float pos_x = -0.7f;
		float pos_z = -0.7f;
		Box[][] schachbrett = new Box[x][y];
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				if ((i + j + 1) % 2 == 0) {
					schachbrett[i][j] = new Box(0.1f, 0.02f, 0.1f,
							Box.GENERATE_NORMALS, createAppearance(0, 0, 0.2f,
									50));
				} else {
					schachbrett[i][j] = new Box(0.1f, 0.02f, 0.1f,
							Box.GENERATE_NORMALS, createAppearance(1, 1, 0, 50));
				}
				Transform3D pos_felder = new Transform3D();
				pos_felder.setTranslation(new Vector3f(pos_x, 0, pos_z));
				TransformGroup pos_schachbrett = new TransformGroup(pos_felder);
				pos_schachbrett.addChild(schachbrett[i][j]);
				objSpin.addChild(pos_schachbrett);
				pos_x += 0.2f;
			}
			pos_x = -0.7f;
			pos_z += 0.2f;
		}

		// Damesteine erstellen

		// alle Steine verschieben
		Transform3D allesteine = new Transform3D();
		allesteine.setTranslation(new Vector3f(0, 0.05f, 0));
		TransformGroup xallesteine = new TransformGroup(allesteine);
		objSpin.addChild(xallesteine);

		// Weiße Steine erstellen
		pos_x = -0.7f;
		pos_z = -0.7f;
		Cylinder[][] w_steine = new Cylinder[3][4];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				w_steine[i][j] = new Cylinder(0.1f, 0.05f,
						Cylinder.GENERATE_NORMALS,
						createAppearance(1, 1, 1, 50));
//				SpotLight spotLight = new SpotLight(new Color3f(1, 1, 1),
//						new Point3f(0, 0.5f, 0), new Point3f(0.3f, 0.3f, 0.3f),
//						new Vector3f(0, -1, 0), 0.2f, 0);
//				spotLight.setInfluencingBounds(new BoundingSphere());
//				w_steine[0][0].addChild(spotLight);

				Transform3D pos_w_stein = new Transform3D();
				pos_w_stein.setTranslation(new Vector3f(pos_x, 0, pos_z));
				TransformGroup pos_w_steine = new TransformGroup(pos_w_stein);
				pos_w_steine.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//				 
//				Figuren verschieben
				Matrix4f richt_mat = new Matrix4f(
						 1,0,0,-3,
						 0,1,0,-30,
						 0,0,1,0,
						 0,0,0,1);
				 Transform3D richtung = new Transform3D(richt_mat);
				 Alpha positionAlpha = new Alpha(-1, 3000);
				 positionAlpha.setPhaseDelayDuration(3000);
				 PositionInterpolator positioner = new PositionInterpolator(positionAlpha, pos_w_steine, richtung, 0f, 0.5f);
				 positioner.setSchedulingBounds(new BoundingSphere());
				 w_steine[0][0].addChild(positioner);
				
				 
				 
				 pos_w_steine.addChild(w_steine[i][j]);
				xallesteine.addChild(pos_w_steine);
				pos_x += 0.4f;
			}
			pos_x = -0.7f;
			if (i == 0) {
				pos_x = -0.5f;
			}
			pos_z += 0.2f;
		}

		// Schwarze Steine erstellen
		pos_x = 0.7f;
		pos_z = 0.7f;
		Cylinder[][] s_steine = new Cylinder[3][4];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				s_steine[i][j] = new Cylinder(0.1f, 0.05f,
						Cylinder.GENERATE_NORMALS,
						createAppearance(0, 0, 0, 50));
				Transform3D pos_s_stein = new Transform3D();
				pos_s_stein.setTranslation(new Vector3f(pos_x, 0, pos_z));
				TransformGroup pos_s_steine = new TransformGroup(pos_s_stein);
				pos_s_steine.addChild(s_steine[i][j]);
				xallesteine.addChild(pos_s_steine);
				pos_x -= 0.4f;
			}
			pos_x = 0.7f;
			if (i == 0) {
				pos_x = 0.5f;
			}
			pos_z -= 0.2f;
		}
		// setup a directional light source
		DirectionalLight dLight = new DirectionalLight();
		dLight.setInfluencingBounds(new BoundingSphere());
		dLight.setColor(new Color3f(1.7f, 1.7f, 2f));

		Vector3f dir_light = new Vector3f(-1.0f, -3.0f, -1.0f);
		dir_light.normalize();
		dLight.setDirection(dir_light);
		objRotate.addChild(dLight);

//		// setup the continuous rotation of the scene around the y-axis
//		 Transform3D yAxis = new Transform3D();
//		 Alpha rotationAlpha = new Alpha(-1, 4000);
//		 RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, objSpin, yAxis, 0.0f, (float) Math.PI * 2.0f);
		 // Figuren verschieben
//		 Transform3D xAxis = new Transform3D();
//		 Alpha positionAlpha = new Alpha(-1, 4000);
//		 PositionInterpolator positioner = new PositionInterpolator(positionAlpha, objSpin, xAxis, 0f, 1f);
//		
		 // set the bounding sphere of the scene
		//BoundingSphere bounds = new BoundingSphere();
//		rotator.setSchedulingBounds(bounds);
//		objSpin.addChild(rotator);
//		positioner.setSchedulingBounds(bounds);
//		objSpin.addChild(positioner);

		// finally, compile the finnished tree (Java3D performs optimizations
		// here)
		objRoot.compile();

		return objRoot;
	}

	public Spielfeld_vic() {
		// setup the window layout, create the Canvas3D object and add it to the
		// window
		setLayout(new BorderLayout());
		Canvas3D canvas = new Canvas3D(SimpleUniverse
				.getPreferredConfiguration());
		add("Center", canvas);

		// create the scenegraph
		BranchGroup scene = createSceneGraph();

		// create the universe for our world
		SimpleUniverse universe = new SimpleUniverse(canvas);

		// set the viewing platform (i.e. the "view" side of the scene graph)
		universe.getViewingPlatform().setNominalViewingTransform();

		// finally, add the scenegraph to the universe
		universe.addBranchGraph(scene);

		// Mausbewegung
		ViewingPlatform viewingPlatform = universe.getViewingPlatform();
		viewingPlatform.setNominalViewingTransform();

		OrbitBehavior orbit = new OrbitBehavior(canvas,
				OrbitBehavior.REVERSE_ALL);
		orbit.setSchedulingBounds(new BoundingSphere(new Point3d(), 100));
		viewingPlatform.setViewPlatformBehavior(orbit);
	}

	public static void main(String[] argv) {
		// simply create a new instance of the window, here
		new MainFrame(new Spielfeld_vic(), 600, 600);
	}

	/**
	 * Creates a simple color and shading-only appearance object using the given
	 * color.
	 * 
	 * @param r
	 *            the red value of the desired object color
	 * @param g
	 *            the green value of the desired object color
	 * @param b
	 *            the blue value of the desired object color
	 * @param s
	 *            the shininess of the object
	 * @return the created Appearance object
	 */
	private Appearance createAppearance(float r, float g, float b, float s) {
		Appearance app = new Appearance();

		Material mat = new Material();
		mat.setShininess(s);
		mat.setDiffuseColor(new Color3f(r, g, b));
		mat.setSpecularColor(new Color3f(0.0f, 0.0f, 0.0f));

		app.setMaterial(mat);
		return (app);
	}
}