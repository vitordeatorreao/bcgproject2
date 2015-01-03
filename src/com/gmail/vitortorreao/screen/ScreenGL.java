package com.gmail.vitortorreao.screen;
import net.letskit.redbook.glskeleton;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.imageio.ImageIO;
import javax.media.opengl.*;
import javax.media.opengl.awt.*;
import javax.media.opengl.glu.*;

import com.gmail.vitortorreao.math.Vector;
import com.gmail.vitortorreao.math.Vertex;
import com.gmail.vitortorreao.scene.Camera;
import com.gmail.vitortorreao.scene.Light;
import com.gmail.vitortorreao.scene.NonConformantSceneFile;
import com.gmail.vitortorreao.scene.Scene;
import com.gmail.vitortorreao.scene.SceneController;
import com.gmail.vitortorreao.scene.Triangle;

public class ScreenGL//
        extends glskeleton//
        implements GLEventListener//
        , KeyListener//
        , MouseListener//
        , MouseMotionListener
{
	
	private static ScreenGL screen;
	private static JFrame frame;
	private static GLJPanel canvas;
	
	private static final double Z_SPEED = 1.0;
	private static final double X_SPEED = 1.0;
	private static final double MOUSE_PAN_SPEED = 0.01;
	
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
//    	try {
//			SceneController.getInstance().loadScene(new File("samples/calice2.byu"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NonConformantSceneFile e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    }
    
    public static void main(String[] args) {
        GLCapabilities caps = new GLCapabilities(GLProfile.getDefault());
        canvas = new GLJPanel(caps);
        screen = new ScreenGL();
        screen.setCanvas(canvas);
        canvas.addGLEventListener(screen);
        screen.setDefaultListeners(screen);
        
//        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("Vitor Torreao's BCG Project 2");
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(canvas);
        
      //Create menu bar
        JMenuBar menuBar = new JMenuBar();
        ImageIcon appIcon;
        ImageIcon openIcon;
        ImageIcon saveIcon;
        ImageIcon exitIcon;
        try {
        	appIcon = new ImageIcon(ScreenGL.class.getResource(
        			"/com/gmail/vitortorreao/images/logo.png"));
        	openIcon = new ImageIcon(ScreenGL.class.getResource(
        			"/com/gmail/vitortorreao/images/open.png"));
        	saveIcon = new ImageIcon(ScreenGL.class.getResource(
        			"/com/gmail/vitortorreao/images/save.png"));
        	exitIcon = new ImageIcon(ScreenGL.class.getResource(
        			"/com/gmail/vitortorreao/images/exit.png"));
        } catch (Exception e){
        	appIcon = new ImageIcon("logo.png");
        	openIcon = new ImageIcon("open.png");
        	saveIcon = new ImageIcon("save.png");
        	exitIcon = new ImageIcon("exit.png");
        }
        
        frame.setIconImage(appIcon.getImage());
        
        JMenu file = new JMenu("File");
        
      //Create Open File Menu Item
        JMenuItem openMenuItem = new JMenuItem("Open", openIcon);
        openMenuItem.setToolTipText("Load objects from BYU files");
        openMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser openFile = new JFileChooser();
				int returnVal = openFile.showOpenDialog(null);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = openFile.getSelectedFile();
					try {
						SceneController.getInstance().loadScene(file);
				        screen.loadCamera();
				        screen.loadLight();
						screen.refresh();
					} catch (IOException | NonConformantSceneFile e) {
						JOptionPane.showMessageDialog(frame, 
								e.getMessage(), "Error while opening file", 
								JOptionPane.ERROR_MESSAGE);
					}
				}
				
			}
		});
        
      //Create Save menu item
        JMenuItem saveMenuItem = new JMenuItem("Save", saveIcon);
        saveMenuItem.setToolTipText("Save view into an image file");
        saveMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser saveFile = new JFileChooser();
				saveFile.addChoosableFileFilter(new FileFilter() {
					
					@Override
					public String getDescription() {
						return "*.png, *.PNG";
					}
					
					@Override
					public boolean accept(File arg0) {
						if (arg0.isDirectory()) {
							return false;
						}
						String filename = arg0.getName();
						return filename.endsWith(".png") || 
								filename.endsWith(".PNG");
					}
				});
				int returnVal = saveFile.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = saveFile.getSelectedFile();
					if (!file.getName().endsWith(".png") && 
							!file.getName().endsWith(".PNG")) {
						file = new File(file.getAbsolutePath()+".png");
					}
					BufferedImage im = new BufferedImage(
							canvas.getWidth(), 
							canvas.getHeight(), 
							BufferedImage.TYPE_INT_RGB);
					canvas.paint(im.getGraphics());
					
					try {
						ImageIO.write(im, "png", file);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(frame, e.getMessage(), 
								"Error while saving file", 
								JOptionPane.ERROR_MESSAGE);
					}
				}
				
			}
		});
        
      //Create Exit program menu item
        JMenuItem exitMenuItem = new JMenuItem("Exit", exitIcon);
        exitMenuItem.setToolTipText("Exit application");
        exitMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
				
			}
		});
        
      //Add menu items to Menu
        file.add(openMenuItem);
        file.add(saveMenuItem);
        file.add(exitMenuItem);
        
        menuBar.add(file);
        
        frame.setJMenuBar(menuBar);
        
        frame.setVisible(true);
        canvas.requestFocusInWindow();
    }

	/*
     * Initialize material property, light source, lighting model, and depth
     * buffer.
     */
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();
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
        Scene scene = SceneController.getInstance().getScene();
        if (scene != null) {
        	Camera camera = scene.getCamera();
        	Light light = scene.getLight();
            if (camera != null) {
            	gl.glMatrixMode(GL2.GL_PROJECTION);
                gl.glLoadIdentity();
                glu.gluPerspective(camera.getFovy(), camera.getAspect(), 
                		camera.getNear(), camera.getFar());
                gl.glMatrixMode(GL2.GL_MODELVIEW);
                
            	gl.glMatrixMode(GL2.GL_MODELVIEW);
            }
            if (light != null) {
            	float[] mat_shininess = new float[] {light.getN()};
        		//
        		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light.getpL(), 0);
        		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, light.getiAmb(), 0);
        		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light.getiDiffuse(), 0);
        		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, light.getiSpecular(), 0);
        		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, light.getmAmb(), 0);
        		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, light.getmDiffuse(), 0);
                gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, light.getmSpecular(), 0);
                gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, light.getmEmissive(), 0);
                gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);
                
            }
        }
        //
    	calcXMov();calcZMov();
    	calcRotation();
    	gl.glLoadIdentity();
    	glu.gluLookAt(eyex, eyey, eyez, centerx, 
    			centery, centerz, upx, upy, upz);
    	//
        drawObjects();
        gl.glFlush();
    }

	protected void loadCamera() {
		Scene scene = SceneController.getInstance().getScene();
		if (scene == null) {
			return;
		}
		Camera camera = scene.getCamera();
		if (camera == null) {
			return;
		}
		eyex = camera.getFocus().getCoord(0);
		eyey = camera.getFocus().getCoord(1);
		eyez = camera.getFocus().getCoord(2);
		//
		centerx = eyex+camera.getN().get(0);
		centery = eyey+camera.getN().get(1);
		centerz = eyez+camera.getN().get(2);
		//
		upx = camera.getV().get(0);
		upy = camera.getV().get(1);
		upz = camera.getV().get(2);
		
		System.out.println("Camera: \nEye = ("+eyex+", "+eyey+", "+eyez+")\n"+
				"Center = ("+centerx+", "+centery+", "+centerz+
				")\n"+
				"Up Vector = ["+upx+", "+upy+", "+upz+")\n\n");
		
	}
	
    
    protected void loadLight() {
    	Scene scene = SceneController.getInstance().getScene();
		if (scene == null) {
			return;
		}
		Light light = scene.getLight();
		if (light == null) {
			return;
		}
		
		
	}

	private void drawObjects() {

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
    	Vertex oldCenter = new Vertex(new double[] {
    			centerx, centery, centerz
    	});
    	centerx += distanceVector.get(0);
    	centery += distanceVector.get(1);
    	Vertex newCenter = new Vertex(new double[] {
    			centerx, centery, centerz
    	});
    	Vector centerMov = newCenter.subtract(oldCenter);
    	
    	Vector upVector = new Vector(new double[] {
    			upx, upy, upz
    	});
    	Vector upMov = upVector.add(centerMov);
    	upMov = upMov.normalize();
    	upx = upMov.get(0);
    	upy = upMov.get(1);
    	upz = upMov.get(2);
    	
    	distanceVector = null;
    	
	}

	private void calcXMov() {
		if (xMov == 0) {
			return;
		}
		Vertex center = new Vertex(new double[] {
				centerx, centery, centerz
		});
		Vertex eye = new Vertex(new double[] {
				eyex, eyey, eyez
		});
		Vector N = center.subtract(eye);
		N = N.normalize();
		Vector up = new Vector(new double[] {
				upx, upy, upz
		});
		Vector U = N.vectorProduct(up);
		if (xMov > 0) {
			U = U.mult(-1.0);
		}
		eyex += U.get(0);
		eyey += U.get(1);
		eyez += U.get(2);
		centerx += U.get(0);
		centery += U.get(1);
		centerz += U.get(2);
		
		xMov = 0;
	}

	private void calcZMov() {
		if (zMov == 0) {
			return;
		}
		Vertex center = new Vertex(new double[] {
				centerx, centery, centerz
		});
		Vertex eye = new Vertex(new double[] {
				eyex, eyey, eyez
		});
		Vector dis = center.subtract(eye);
		dis = dis.normalize();
		if (zMov > 0) {
			//Pressed S
			dis = dis.mult(-1.0);
		}
		
		eyex += dis.get(0);
		eyey += dis.get(1);
		eyez += dis.get(2);
		centerx += dis.get(0);
		centery += dis.get(1);
		centerz += dis.get(2);
		
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
