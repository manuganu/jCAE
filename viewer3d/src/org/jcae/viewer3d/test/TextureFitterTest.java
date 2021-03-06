package org.jcae.viewer3d.test;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import org.jcae.opencascade.Utilities;
import org.jcae.opencascade.jni.*;
import org.jcae.viewer3d.SelectionListener;
import org.jcae.viewer3d.Viewable;
import org.jcae.viewer3d.cad.CADSelection;
import org.jcae.viewer3d.cad.ViewableCAD;
import org.jcae.viewer3d.cad.occ.OCCProvider;
import org.jcae.viewer3d.post.TextureFitter;

/**
 * A demo of the TextureFitter class
 * @author Jerome Robert
 */
public class TextureFitterTest
{
	private ViewableCAD fullViewable;
	private TopoDS_Shape fullShape;
	
	private TextureFitter.PickViewableCAD faceViewable;
	private TopoDS_Shape faceShape;

	private TextureFitter view;
	private int step=0;
	private BufferedImage image;
	
	/*private static Point3d[] point3ds=new Point3d[]{
		new Point3d(105.876103680699, 154.355112438377, 444.233449262934),
		new Point3d(15649.9293772451, 170.548470749469, 494.066332188722),
		new Point3d(5504.83546309108, 31.9677707249711, 67.6028607292355)};

	private static Point3d[] point2ds=new Point3d[]{
		new Point3d(7836, 408, 0),
		new Point3d(66, 380, 0),
		new Point3d(5137, 605, 0)};*/

	/*private static Point3d[] point3ds=new Point3d[]{
		new Point3d(15425.4735650595, 101.630084521872, 281.979264993215),
		new Point3d(6972.95999971717, 93.695636325025, 257.562067493599),
		new Point3d(834.41408284612, 172.766820120453, 500.893004108474)};

	private static Point3d[] point2ds=new Point3d[]{
		new Point3d(178, 491, 0),
		new Point3d(4405, 505, 0),
		new Point3d(7474, 378, 0)};*/
	
	private static Point3d[] point2ds=new Point3d[]{
		new Point3d(80, 40, 0),
		new Point3d(460, 110, 0),
		new Point3d(48, 640, 0)};

	private static Point3d[] point3ds=new Point3d[]{
		new Point3d(5317, -907, 1692),
		new Point3d(5206, -1471, 1207),
		new Point3d(4292, -784, 1615)};
	private Viewable viewable;
	private String bitmap;


	/**
	 * Add interactions with the view.
	 * Pressing space will allow to pick vertex, then display the texture.
	 * + - and direction keys allow to manually fit the texture.
	 * @author Jerome Robert
	 */
	private class MyKeyListener extends KeyAdapter
	{
		private int currentPoint=0;
		public void keyPressed(KeyEvent e)
		{
			System.out.println(e.getKeyCode());
			if(e.getKeyCode()==KeyEvent.VK_SPACE)
			{
				switch(step)
				{
					case 0:
						firstStep();
						step++;
						break;
					case 1:
						secondStep();
						TextureFitter.displayMatrixInfo(
							TextureFitter.getTransform(point2ds, point3ds));
						step++;
						break;
				}
			}
			
			boolean changed=false;
			
			switch(e.getKeyCode())
			{
				case KeyEvent.VK_UP:
					point2ds[currentPoint].y--;
					changed=true;
					break;
				case KeyEvent.VK_DOWN:
					point2ds[currentPoint].y++;
					changed=true;
					break;
				case KeyEvent.VK_LEFT:
					point2ds[currentPoint].x--;
					changed=true;
					break;
				case KeyEvent.VK_RIGHT:
					point2ds[currentPoint].x++;
					changed=true;
					break;				
				case KeyEvent.VK_ADD:
					currentPoint++;
					changed=true;
					break;				
				case KeyEvent.VK_SUBTRACT:
					currentPoint--;
					changed=true;
					break;				
			}
			
			if(changed)
			{				
				System.out.println("Point "+currentPoint+" : "+
					point2ds[currentPoint].x +" "+point2ds[currentPoint].y);
				view.updateTexture(viewable, point2ds, point3ds);
				TextureFitter.displayMatrixInfo(
					TextureFitter.getTransform(point2ds, point3ds));

			}
		}
	}
	
	/**
	 * Replace the full geometry by the selected faces
	 */
	private void firstStep()
	{
		//Get selected faces
		faceShape=TextureFitter.getSelectFaces(fullViewable, fullShape);
		//faceShape=fullShape;
		//save it for later
		BRepTools.write(faceShape, "toto.brep");
		
		//remove the full geometry view
		view.remove(fullViewable);
		
		//display only the selected faces
		//faceViewable=new ViewableCAD(new OCCProvider(faceShape));
		faceViewable=new TextureFitter.PickViewableCAD(new OCCProvider(faceShape), faceShape);
		view.add(faceViewable);
		
		// switch from face picking mode to vertex picking mode
		//faceViewable.setSelectionMode(ViewableCAD.VERTEX_SELECTION);
		
		// display the coordinates of the selected vertex
		faceViewable.addSelectionListener(new VertexSelectionListener());
		faceViewable.addSelectionListener(new PointSelectionListener());
	}
	
	/**
	 * Display the selected the texture
	 */
	private void secondStep()
	{
		try
		{
			//display the texture
			image=ImageIO.read(new File(bitmap));
			viewable = view.displayTexture(faceShape, point2ds, point3ds, image, false);
			Matrix4d m=TextureFitter.getTransform(point2ds, point3ds, false);
			System.out.println("orthogonality: "+TextureFitter.getOrthogonality(m));
			System.out.println("scaling: "+TextureFitter.getScaling(m));
			//and the full CAD
			/*view.remove(faceViewable);
			view.add(fullViewable);*/
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * A simple SelectionListener which display the picked coordinates of vertices
	 * in the console
	 * @author Jerome Robert
	 */
	private class VertexSelectionListener implements SelectionListener
	{
		public void selectionChanged()
		{					
			CADSelection[] ss=faceViewable.getSelection();
			for(int i=0; i<ss.length; i++)
			{
				int[] ids=ss[i].getVertexIDs();
				for(int j=0; j<ids.length; j++)
				{
					TopoDS_Vertex v=Utilities.getVertex(faceShape, ids[j]);
					//Get the coordinates if each selected vertices
					double[] coords=BRep_Tool.pnt(v);
					System.out.println("Vertex selected: "+ids[j]+
						" ("+coords[0]+", "+coords[1]+", "+coords[2]+")");
				}
			}
		}		
	}

	/**
	 * A simple SelectionListener which display the picked coordinates on faces
	 * in the console
	 * @author Jerome Robert
	 */
	private class PointSelectionListener implements SelectionListener
	{
		public void selectionChanged()
		{					
			double[] point = faceViewable.getLastPick();
			if(point!=null)
				System.out.println("Point selected on the surface: "+point[0]+" "+point[1]+" "+point[2]);
	
		}		
	}

	// CAD: /home/jerome/ndtkit/19250L57110323000.igs
	// CAD: /home/jerome/ndtkit/x53p11431200.igs
	// BMP: /home/jerome/ndtkit/DC2-Amp-bis.bmp
	// BMP: /home/jerome/ndtkit/190887A.png
	public TextureFitterTest(String cad, String bitmap) throws IOException
	{
		this.bitmap = bitmap;
		JFrame frame = new JFrame();
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		view = new TextureFitter(frame);

		long time = System.currentTimeMillis();
		long mem =
			Runtime.getRuntime().totalMemory() -
			Runtime.getRuntime().freeMemory();

		if(!new File(cad).canRead())
			throw new IOException("Cannot read "+cad);
		
		fullShape = Utilities.readFile(cad);
		
		OCCProvider occProvider = new OCCProvider(fullShape);
		fullViewable = new ViewableCAD(occProvider);
		view.add(fullViewable);
		frame.add(view);
		long mem2 =
			Runtime.getRuntime().totalMemory() -
			Runtime.getRuntime().freeMemory();
		System.out.println("time to load: " +
			(System.currentTimeMillis() - time));
		System.out.println("used memory: " + (mem2 - mem));
		//Fit the view to the geometry
		view.fitAll();
		frame.setVisible(true);
		view.setOriginAxisVisible(true);
		view.addKeyListener(new MyKeyListener());
		view.print3DProperties(System.out);
		view.drawPoint(0, 0, 0);
		view.drawPoint(1000, 1000, 1000);
	}

	public static void main(String[] args)
	{
		try
		{
			new TextureFitterTest(args[0], args[1]);
		}
		catch (IOException ex)
		{
			Logger.getLogger(TextureFitterTest.class.getName()).
				log(Level.SEVERE, null, ex);
		}
	}
}
