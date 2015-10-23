import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class filterLoader extends JLabel{
	
	private static final long serialVersionUID = 1L;  
		
    static JFrame frame = null;
    static JLabel jLabel = new JLabel();
	static BufferedImage resizedImage;
	static ImageIcon imageIcon;
	static Graphics2D g;
	static int widhtFrame = 640;
	static int heightFrame = 480;
	static Size size;
	static Mat matResizeOriginal;
	static Mat matGray, matGrayTemp;
	static Mat matColor;
	static Mat matOriginal;
	static Mat mergImg;
	static List<Mat> lRgb;
	static Mat mAddTo;
	//Dimension of Desktop Screen
    static Dimension dim;
    static BufferedImage temp;
	
    static JPopupMenu popup;
    static boolean originalFlag = false, grayFlag = false, colorFlag = false;
    static JFileChooser fc = new JFileChooser();
    // Create a constructor method  
    public filterLoader(){
      super();
    } 

    //!Inic Layout
    public static void layoutIni(){
    	dim = Toolkit.getDefaultToolkit().getScreenSize();
    	matResizeOriginal = new Mat(heightFrame, widhtFrame, CvType.CV_8UC3);
    	//Display in JFrame
    	frame = new JFrame("Filter Loader");
    	frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    	frame.setResizable(true);
    	frame.setSize(widhtFrame + 10, heightFrame + 35);
    	frame.addComponentListener(new ComponentAdapter() {  
    		public void componentResized(ComponentEvent evt) {
    			Component c = evt.getComponent();
    		    widhtFrame = c.getSize().width;
    		    heightFrame = c.getSize().height;
    		    //System.out.println(c.getSize());  		    
    		}
    	});
    	frame.setVisible(true);
    	frame.setFocusable(true);
    	
    	frame.addWindowListener(new java.awt.event.WindowAdapter() {
    	    @Override
    	    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
    	    	if (JOptionPane.showConfirmDialog(frame,"Are you sure to close this window?", "Really Closing?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
    	    		System.out.println("DONE...");
    	    		System.exit(0);
    	    	}
    	    	else {
    	    		frame.setVisible(true);
    	        }
    	    }
    	});
    	
    	size = new Size(widhtFrame, heightFrame);
    	
    	//Mouse click popup menu
    	frame.addMouseListener(new MouseListener() {
    		public void mousePressed(MouseEvent e) {
    		}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getButton() == MouseEvent.BUTTON3) {
					popup = new JPopupMenu();
                    @SuppressWarnings("unused")
					JMenuItem item1;
                    popup.add(item1 = new JMenuItem("Original Frame")).addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                        	//System.out.println("Original");
                        	originalFlag = true;
                        	grayFlag = false;
                        	colorFlag = false;
                        }
                    });
                    @SuppressWarnings("unused")
					JMenuItem item2;
                    popup.add(item2 = new JMenuItem("GrayHist Frame")).addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                        	//System.out.println("Gray");
                        	originalFlag = false;
                        	grayFlag = true;
                        	colorFlag = false;
                        }
                    });
                    @SuppressWarnings("unused")
					JMenuItem item3;  
                    popup.add(item3 = new JMenuItem("Color Frame")).addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                        	//System.out.println("Color");
                        	originalFlag = false;
                        	grayFlag = false;
                        	colorFlag = true;
                        }
                    });
                    popup.addSeparator();
                    @SuppressWarnings("unused")
					JMenuItem item4;  
                    popup.add(item4 = new JMenuItem("Load Image")).addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                        	//System.out.println("Load");
                        	FileNameExtensionFilter filter = new FileNameExtensionFilter("IMAGE FILES", "png", "jpeg", "bmp", "jpg", "ico");
            				fc.setFileFilter(filter);
            				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            				int status = fc.showOpenDialog(null);
            				if (status == JFileChooser.APPROVE_OPTION) {
            				      File selectedFile = fc.getSelectedFile();
            				      /*System.out.println(selectedFile.getParent());
            				      System.out.println(selectedFile.getName());
            				      System.out.println(selectedFile);*/
            				      
            				      //LOAD
            				      matOriginal = Highgui.imread(String.valueOf(selectedFile) ,Highgui.CV_LOAD_IMAGE_COLOR);
            				} 
            				else if (status == JFileChooser.CANCEL_OPTION) {
            				      System.out.println("JFileChooser.CANCEL_OPTION");
            				}
            				
                        	originalFlag = true;
                        	grayFlag = false;
                        	colorFlag = false;
                        }
                    });
                    popup.show((Component) e.getSource(), e.getX(), e.getY());
				}
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {				
			}

			@Override
			public void mouseEntered(MouseEvent e) {				
			}

			@Override
			public void mouseExited(MouseEvent e) {				
			}
    	});
    }
    
    //TODO: MAIN
	public static void main(String[] args) throws Exception {
		System.out.println(System.getProperty("java.library.path"));
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		layoutIni();
		
		while(true){
			if(originalFlag){
				showOneImag(matOriginal);
				Thread.sleep(100);;
			}
			else if(grayFlag){
				showOneImag(grayHist(matOriginal));
				Thread.sleep(100);
			}
			else if(colorFlag){
				showOneImag(colorHist(matOriginal));
				Thread.sleep(100);
			}
			else
				Thread.sleep(100);
		}
    }
		
	public static void showOneImag(Mat img){
		size = new Size(widhtFrame, heightFrame);
		Imgproc.resize(img, matResizeOriginal, size);
		temp=matToBufferedImage(matResizeOriginal);
		showImage(temp);
	}
	
	//!GRAY
	public static Mat grayHist(Mat img){
		matGrayTemp = new Mat(img.rows(), img.cols(), CvType.CV_8UC1);
		matGray = new Mat(img.rows(), img.cols(), CvType.CV_8UC3);
		Imgproc.cvtColor(img, matGrayTemp, Imgproc.COLOR_RGB2GRAY);
		Imgproc.equalizeHist(matGrayTemp, matGrayTemp);
		Imgproc.cvtColor(matGrayTemp, matGray, Imgproc.COLOR_GRAY2RGB);
		
		return matGray;
	}
	
	//!COLOR
	public static Mat colorHist(Mat img){
		matColor = new Mat(img.rows(), img.cols(), CvType.CV_8UC3);
		lRgb = new ArrayList<Mat>(3);
		Core.split(img, lRgb);
		Mat mR = lRgb.get(0);
		Imgproc.equalizeHist(mR, mR);
		lRgb.set(0, mR);
		Mat mG = lRgb.get(1);
		Imgproc.equalizeHist(mG, mG);
		lRgb.set(1, mG);
		Mat mB = lRgb.get(2);
		Imgproc.equalizeHist(mB, mB);
		lRgb.set(2, mB);
		Core.merge(lRgb, matColor);
		
		return matColor;
	}
	
	//!Merge img
	public static Mat addTo(Mat matA, Mat matB, Mat matC) {
		mAddTo = new Mat(matA.rows(), matA.cols() +  matB.cols() +  matC.cols(), matA.type());
	    int aCols = matA.cols();
	    int aRows = matA.rows();
	    for (int i = 0; i < aRows; i++) {
	        for (int j = 0; j < aCols; j++) {
	        	mAddTo.put(i, j, matA.get(i, j));
	        }
	    }
	    for (int i = 0; i < matB.rows(); i++) {
	        for (int j = 0; j < matB.cols(); j++) {
	        	mAddTo.put(i, aCols + j, matB.get(i, j));
	        }
	    }
	    for (int i = 0; i < matC.rows(); i++) {
	        for (int j = 0; j < matC.cols(); j++) {
	        	mAddTo.put(i, aCols*2 + j, matC.get(i, j));
	        }
	    }
	    return mAddTo;
	}
	//!Show Image
	public static void showImage(final BufferedImage image){
		int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
		resizedImage = new BufferedImage(widhtFrame, heightFrame, type);
		g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, widhtFrame, heightFrame, null);
		g.dispose();
		imageIcon = new ImageIcon(resizedImage);
	    imageIcon.getImage().flush();
	    jLabel.setIcon(imageIcon);
	    frame.revalidate();
	    frame.add(jLabel, BorderLayout.CENTER);
		}
	
	//!Convert bufferedImage to Mat
	public static Mat bufferedImageToMat(BufferedImage in)
    {
          Mat out;
          byte[] data;
          int r, g, b;

          if(in.getType() == BufferedImage.TYPE_INT_RGB)
          {
              out = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC3);
              data = new byte[in.getWidth() * in.getHeight() * (int)out.elemSize()];
              int[] dataBuff = in.getRGB(0, 0, in.getWidth(), in.getHeight(), null, 0, in.getWidth());
              for(int i = 0; i < dataBuff.length; i++)
              {
                  data[i*3] = (byte) ((dataBuff[i] >> 16) & 0xFF);
                  data[i*3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
                  data[i*3 + 2] = (byte) ((dataBuff[i] >> 0) & 0xFF);
              }
          }
          else
          {
              out = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC1);
              data = new byte[in.getWidth() * in.getHeight() * (int)out.elemSize()];
              int[] dataBuff = in.getRGB(0, 0, in.getWidth(), in.getHeight(), null, 0, in.getWidth());
              for(int i = 0; i < dataBuff.length; i++)
              {
                r = (byte) ((dataBuff[i] >> 16) & 0xFF);
                g = (byte) ((dataBuff[i] >> 8) & 0xFF);
                b = (byte) ((dataBuff[i] >> 0) & 0xFF);
                data[i] = (byte)((0.21 * r) + (0.71 * g) + (0.07 * b)); //luminosity
              }
           }
           out.put(0, 0, data);
           return out;
     } 
    
    /**  
     * Converts/writes a Mat into a BufferedImage.  
     * @param matrix Mat of type CV_8UC3 or CV_8UC1  
     * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY  
     */  
    public static BufferedImage matToBufferedImage(Mat matrix) {
        int cols = matrix.cols();  
        int rows = matrix.rows();  
        int elemSize = (int)matrix.elemSize();  
        byte[] data = new byte[cols * rows * elemSize];  
        int type;  
        matrix.get(0, 0, data);  
        switch (matrix.channels()) {  
            case 1:  
                type = BufferedImage.TYPE_BYTE_GRAY;  
                break;  
            case 3:  
                type = BufferedImage.TYPE_3BYTE_BGR;  
                // bgr to rgb  
                byte b;  
                for(int i=0; i<data.length; i=i+3) {  
                    b = data[i];  
                    data[i] = data[i+2];  
                    data[i+2] = b;  
                }  
                break;  
        default:  
            return null;  
        }
        BufferedImage image2 = new BufferedImage(cols, rows, type);  
        image2.getRaster().setDataElements(0, 0, cols, rows, data);  
        return image2;
    }
} 
