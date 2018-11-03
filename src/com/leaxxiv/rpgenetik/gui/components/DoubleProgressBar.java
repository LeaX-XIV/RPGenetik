package com.leaxxiv.rpgenetik.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

public class DoubleProgressBar extends JComponent {
	
//	private static Image bg; 
//	
//	static {
//		try {
//			bg = ImageIO.read(DoubleProgressBar.class.getResource("/img/bg.jpg"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			bg = null;
//		}
//	}

	private Dimension dimensions = new Dimension(300, 20);

	private int min;
	private int max;
	
	private double topValue;
	private double bottomValue;
	
	private double topTarget;
	private double bottomTarget;
	
	private Color topColor = Color.YELLOW;
	private Color bottomColor = Color.RED;
	
	
	public DoubleProgressBar(int min, int max) {
		this.min = min;
		this.max = max;
		
		
		this.topValue = this.min;
		this.bottomValue = this.min;
		
		this.topTarget = this.topValue;
		this.bottomTarget = this.bottomValue;
	}
	
	public double getTopValue() {
		return this.topValue;
	}
	
	public double getBottomValue() {
		return this.bottomValue;
	}
	
	public double getTopTarget() {
		return this.topTarget;
	}
	
	public double getBottomTarget() {
		return this.bottomTarget;
	}
	
	public void setValues(double topValue, double bottomValue) {
		this.topValue = topValue;
		this.bottomValue = bottomValue;
		
		this.topTarget = topValue;
		this.bottomTarget = bottomValue;
		
		repaint();
	}
	
	public synchronized void setTragetValues(double topTarget, double bottomTarget, int steps) {
		this.topTarget = topTarget;
		this.bottomTarget = bottomTarget;
		
		double topDiff = this.topValue - this.topTarget;
		double bottomDiff = this.bottomValue - this.bottomTarget;
		
		double topDelta = topDiff / steps;
		double bottomDelta = bottomDiff / steps;
		
		new Thread() {
			public void run() {
				for(int i = 0; i < steps; i++) {
					setValues(getTopValue() - topDelta, getBottomValue() - bottomDelta);
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				setValues(getTopTarget(), getBottomTarget());
			};
		}.start();
	}
	
	public void setColors(Color topColor, Color bottomColor) {
		this.topColor = topColor;
		this.bottomColor = bottomColor;
	}
	
	@Override
	public void setSize(Dimension d) {
		this.dimensions = d;
	}
	
	@Override
	public Dimension getPreferredSize() {
	    return dimensions;
	}
	
	@Override
	public Dimension getMaximumSize() {
	    return dimensions;
	}
	
	@Override
	public Dimension getMinimumSize() {
	    return dimensions;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
//		g.drawImage(bg, 0, 0, getWidth(), getHeight(), 0, 0, getWidth(), getHeight(), null);
		
//		g.setColor(Color.GREEN);
//		g.fillRect(0, 0, getWidth()-1, getHeight()-1);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth()-1, getHeight()-1);
	
		g.setColor(bottomColor);
		g.fillRect(1, 1, (int)(getBottomValue() * getWidth() / max -2), getHeight()-2);
		g.setColor(topColor);
		g.fillRect(1, 1, (int)(getTopValue() * getWidth() / max -2), getHeight()-2);
	}

}
