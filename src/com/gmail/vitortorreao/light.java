package com.gmail.vitortorreao;
import net.letskit.redbook.glskeleton;

import java.awt.event.*;

import javax.swing.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.*;
import javax.media.opengl.glu.*;

import com.gmail.vitortorreao.math.Vector;
import com.gmail.vitortorreao.math.Vertex;
import com.jogamp.opengl.util.gl2.*;

public class light//
        extends glskeleton//
        implements GLEventListener//
        , KeyListener//
        , MouseListener//
        , MouseMotionListener
{
	
	private static final double Z_SPEED = 0.5;
	private static final double X_SPEED = 0.1;
	private static final double MOUSE_PAN_SPEED = 0.01;
	private static final double MOUSE_ORBIT_SPEED = 0.01;
	
    private GLUT glut;
    private GL2 gl;
	private GLU glu;
	
	private double	eyex, eyey, eyez, centerx, 
					centery, centerz, upx, upy, upz,
					zMov, xMov;
	
	private Vertex pressed;
	private Vector distanceVector;
	
	private int mouseDown, oldx, oldy;
	
	private double theta, phi;
    
    //
    /**
     * This program demonstrates the use of the OpenGL lighting model. A sphere
     * is drawn using a grey material characteristic. A single light source
     * illuminates the object.
     *
     * @author Kiet Le (Java conversion)
     */
    public light() {
    }
    
    public static void main(String[] args) {
        GLCapabilities caps = new GLCapabilities(GLProfile.getDefault());
        GLJPanel canvas = new GLJPanel(caps);
        light demo = new light();
        demo.setCanvas(canvas);
        canvas.addGLEventListener(demo);
        demo.setDefaultListeners(demo);
        
//        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("light");
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
    	System.out.println("init");
        gl = drawable.getGL().getGL2();
        glut = new GLUT();
        glu = new GLU();
        
        eyex = 0;
        eyey = 0;
        eyez = 40;
        centerx = 0;
        centery = 0;
        centerz = -1;
        upx = 0;
        upy = 1;
        upz = 0;
        
        theta = 0.0;
        phi = 0.0;
        
        //
        float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
        float mat_shininess[] = { 50.0f };
        float light_position[] = { 1.0f, 1.0f, 1.0f, 0.0f };
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
    	System.out.println("display");
    	
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
        gl.glColor4d(1, 0, 0, 0);
        glut.glutSolidSphere(1.0, 20, 20);
        gl.glFlush();
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
    	System.out.println("Reshape");
        //
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(2, ((double) w)/h, 0.1, 100);
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
            	xMov = -X_SPEED;
            	refresh();
            	break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_NUMPAD6:
            	xMov = X_SPEED;
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
			System.out.println(	"Mouse Pressed: "+click.getX()+", "
								+click.getY());
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
			System.out.println("Pan");
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
