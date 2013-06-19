import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.*;

public class SeekBar extends JPanel {
	private int xInset = 1;
	private int yInset = 1;
	private Color seekBarColor = Color.BLUE;
	private int xPercent = 0;
	private boolean releasedFlag = true;
	private Rectangle scrollArea;
	private double value = 100;
	private Image bg;
	
	public SeekBar() {
		setPreferredSize(new Dimension(100,15));
		scrollArea = calculateScrollArea();
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				checkConsistentScrollArea();
				updateXPercent(e.getX());
				releasedFlag = true;
			}
			@Override
			public void mousePressed(MouseEvent e) {
				checkConsistentScrollArea();
				updateXPercent(e.getX());
				releasedFlag = false;
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				checkConsistentScrollArea();
				updateXPercent(e.getX());
				releasedFlag = false;
			}
		});
		
		setBorder(BorderFactory.createEtchedBorder(Color.lightGray, Color.darkGray));
	}
	
	public SeekBar(double val) {
		value = val;
		setPreferredSize(new Dimension(100,15));
		scrollArea = calculateScrollArea();
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				checkConsistentScrollArea();
				updateXPercent(e.getX());
				releasedFlag = true;
			}
			@Override
			public void mousePressed(MouseEvent e) {
				checkConsistentScrollArea();
				updateXPercent(e.getX());
				releasedFlag = false;
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				checkConsistentScrollArea();
				updateXPercent(e.getX());
				releasedFlag = false;
			}
		});
			
		setBorder(BorderFactory.createEtchedBorder(Color.lightGray, Color.darkGray));
	}
	
	@Override
	public void paintComponent(Graphics arg0) {
		checkConsistentScrollArea();
		//make sure back buffer is acceptable
		if(bg == null || bg.getWidth(this) != getWidth() || bg.getHeight(this) != getHeight()) {
			bg = createImage(getWidth(), getHeight());
		}
		
		Graphics2D g = (Graphics2D) bg.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//paint the background
		g.setColor(getBackground());
		g.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
		//paint the seek bar
		g.setColor(getSeekBarColor());
		g.fill(new Rectangle2D.Double(scrollArea.x, scrollArea.y, scrollArea.width*(xPercent/100.0), scrollArea.height));
		
		//draw the selected percentage
		if(!releasedFlag) {
			
		}
		
		arg0.drawImage(bg, 0, 0, null);
		g.dispose();
	}
	
	public void checkConsistentScrollArea() {
		if(scrollArea == null || scrollArea.width != getWidth() - xInset*2 || scrollArea.height != getHeight() - yInset*2) {
			scrollArea = calculateScrollArea();
		}
	}
	
	public void updateXPercent(int x) {
		if(x - xInset > scrollArea.width) {
			x = scrollArea.width + xInset;
		} else if(x - xInset < xInset) {
			x = xInset;
		}
		
		xPercent = (int) (((double)(x-scrollArea.x)) / ((double)scrollArea.width) * 100.0);
		repaint();
	}
	
	public Rectangle calculateScrollArea() {
		return new Rectangle(xInset, yInset, getWidth() - xInset*2, getHeight() - yInset*2);
	}
	
	public double getValue() {
		return (xPercent/100.0)*value;
	}
	
	public double getPercentAsDecimal() {
		return (xPercent/100.0);
	}
	
	public Color getSeekBarColor() {
		return seekBarColor;
	}
	
	public void setSeekBarColor(Color newColor) {
		seekBarColor = newColor;
	}
	
	public int getPercent() {
		return xPercent;
	}
	
	public void setPercent(int val) {
		xPercent = val;
	}
	
	public int getXInset() {
		return xInset;
	}
	
	public void setXInset(int val) {
		xInset = val;
	}
	
	public int getYInset() {
		return yInset;
	}
	
	public void setYInset(int val) {
		yInset = val;
	}
}