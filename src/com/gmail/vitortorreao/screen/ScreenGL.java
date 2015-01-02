package com.gmail.vitortorreao.screen;
import net.letskit.redbook.glskeleton;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.*;
import javax.media.opengl.glu.*;

import jogamp.opengl.GLVersionNumber;

import com.gmail.vitortorreao.math.Vector;
import com.gmail.vitortorreao.math.Vertex;
import com.gmail.vitortorreao.scene.NonConformantSceneFile;
import com.gmail.vitortorreao.scene.SceneController;
import com.gmail.vitortorreao.scene.Triangle;
import com.jogamp.opengl.util.gl2.*;

public class ScreenGL//
        extends glskeleton//
        implements GLEventListener//
        , KeyListener//
        , MouseListener//
        , MouseMotionListener
{
	
	private static final double Z_SPEED = 1.0;
	private static final double X_SPEED = 1.0;
	private static final double MOUSE_PAN_SPEED = 0.01;
	
    private GLUT glut;
    private GL2 gl;
	private GLU glu;
	
	private double	eyex, eyey, eyez, centerx, 
					centery, centerz, upx, upy, upz,
					zMov, xMov;
	
	private Vertex pressed;
	private Vector distanceVector;
	
	private int mouseDown;
    
    //
    /**
     * This program demonstrates the use of the OpenGL lighting model. A sphere
     * is drawn using a grey material characteristic. A single light source
     * illuminates the object.
     *
     * @author Kiet Le (Java conversion)
     */
    public ScreenGL() {
    	try {
			SceneController.getInstance().loadScene(new File("samples/vaso.byu"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NonConformantSceneFile e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public static void main(String[] args) {
        GLCapabilities caps = new GLCapabilities(GLProfile.getDefault());
        GLJPanel canvas = new GLJPanel(caps);
        ScreenGL demo = new ScreenGL();
        demo.setCanvas(canvas);
        canvas.addGLEventListener(demo);
        demo.setDefaultListeners(demo);
        
//        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Vitor Torreao's BCG Project 2");
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.getContentPane().add(canvas);
        frame.setVisible(true);
        canvas.requestFocusInWindow();
    }
    
    /*
     * Initialize material property, light source, lighting model, and depth
     * buffer.
     */
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();
        glut = new GLUT();
        glu = new GLU();
        
        eyex = 0;
        eyey = -500;
        eyez = 500;
        centerx = 0;
        centery = -499;
        centerz = 499;
        upx = 0;
        upy = -1;
        upz = -1;
                
        //
        float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
        float mat_shininess[] = { 20.0f };
        float light_position[] = { 60.0f, 5.0f, -10.0f, 0.0f };
        //
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL2.GL_SMOOTH);
        //
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position, 0);
        //
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LESS);
    }
    
    public void display(GLAutoDrawable drawable) {
    	
        //
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        //
    	gl.glMatrixMode(GL2.GL_MODELVIEW);
    	calcXMov();calcZMov();
    	calcRotation();
    	gl.glLoadIdentity();
    	glu.gluLookAt(eyex, eyey, eyez, centerx, 
    			centery, centerz, upx, upy, upz);
    	//
        createObject();
        gl.glFlush();
    }

	private void createObject() {

        gl.glColor4d(1, 0, 0, 0);
		ArrayList<Triangle> triangles = 
				SceneController.getInstance().getScene().getTriangles();
		for (Triangle t : triangles) {
			gl.glBegin(GL2.GL_TRIANGLES);
				for (Vertex v : t.getVertices()) {
					gl.glNormal3d(v.getNormal().get(0), 
							v.getNormal().get(1), 
							v.getNormal().get(2));
					gl.glVertex3d(v.getCoord(0), 
							v.getCoord(1), v.getCoord(2));
				}
			gl.glEnd();
		}
	}

	private void calcRotation() {
    	if (distanceVector == null) {
    		return;
    	}
    	centerx += distanceVector.get(0);
    	centery += distanceVector.get(1);
    	
	}

	private void calcXMov() {
		centerx += xMov;
		eyex += xMov;
		xMov = 0;
	}

	private void calcZMov() {
		centerz += zMov;
		eyez += zMov;
		zMov = 0;
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        //
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(50, ((double) w)/h, 50, 10000);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }
    
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
            boolean deviceChanged) {
    }
    
    public void keyTyped(KeyEvent key) {
    }
    
    public void keyPressed(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                super.runExit();
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_NUMPAD8:
            	zMov = -Z_SPEED;
            	refresh();
            	break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_NUMPAD2:
            	zMov = Z_SPEED;
            	refresh();
            	break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_NUMPAD4:
            	xMov = X_SPEED;
            	refresh();
            	break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_NUMPAD6:
            	xMov = -X_SPEED;
            	refresh();
            	break;
            default:
                break;
        }
    }
    
    
    
    public void keyReleased(KeyEvent key) {
    }
    
    
    public void dispose(GLAutoDrawable drawable){
    }

	@Override
	public void mouseClicked(MouseEvent click) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent click) {
		switch (click.getButton()) {
		case MouseEvent.BUTTON1:
			pressed = new Vertex(new double[] {
					click.getX(), 
					click.getY()
			});
			
			mouseDown = MouseEvent.BUTTON1;
			break;
		case MouseEvent.BUTTON3:
//			oldx = click.getX();
//			oldy = click.getY();
//			mouseDown = MouseEvent.BUTTON3;
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent click) {
//		switch (click.getButton()) {
//		case MouseEvent.BUTTON1:
//			System.out.println(	"Mouse Released: "+click.getX()+", "
//								+click.getY());
//			
//			break;
//		default:
//			break;
//		}
	}

	@Override
	public void mouseDragged(MouseEvent mouse) {
		switch(mouseDown) {
		case MouseEvent.BUTTON1:
			Vertex drag = new Vertex(new double[] {
					mouse.getX(), 
					mouse.getY()
			});
			distanceVector = drag.subtract(pressed);
			distanceVector = distanceVector.mult(MOUSE_PAN_SPEED);
			distanceVector.set(1, distanceVector.get(1)*-1);
			pressed = new Vertex(new double[] {
					drag.getCoord(0),
					drag.getCoord(1)
			});
			refresh();
			break;
		case MouseEvent.BUTTON3:
//			System.out.println("Orbit");
//			int x = mouse.getX();
//			int y = mouse.getY();
//			Vertex center = new Vertex(new double[] {
//					centerx, centery, centerz
//			});
//			Vertex eye = new Vertex(new double[] {
//					eyex, eyey, eyez
//			});
//			Vertex newEyeX = new Vertex(new double[] {
//					eyex + (x - oldx), eyey, eyez
//			});
//			Vertex newEyeY = new Vertex(new double[] {
//					eyex, eyey + (y - oldy), eyez
//			});			
//			Vector oldVector = center.subtract(eye);
//			Vector newVectorX = center.subtract(newEyeX);
//			double sm = newVectorX.scalarMult(oldVector);
//			double divd = newVectorX.getNorm()*oldVector.getNorm();
//			double costheta = sm/divd;
//			double angle = Math.acos(costheta);
//			if (x > oldx) {
//				//horario
//				angle = - angle;
//			}
//			double[] newCamPos = oldVector.rotate(angle).getArray();
//			eyex = centerx + newCamPos[0];
//			eyey = centery + newCamPos[1];
//			eyez = centerz + newCamPos[2];
//			refresh();
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {	
	}
}
