package ru.spark.slauncher.ui.swing.extended;

import ru.spark.slauncher.ui.swing.util.IntegerArrayGetter;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * <code>ExtendedComponentListener</code> gives ability to track the start and end
 * moments of resizing/moving
 * 
 * @author spark
 * @see #onComponentMoved(ComponentEvent)
 * @see #onComponentResized(ComponentEvent)
 * 
 */
public abstract class ExtendedComponentListener implements ComponentListener {
	private final Component comp;
	private final QuickParameterListenerThread resizeListener, moveListener;
	private ComponentEvent lastResizeEvent, lastMoveEvent;
	
	public ExtendedComponentListener(Component component, int tick) {
		if(component == null)
			throw new NullPointerException();
		
		this.comp = component;
		
		this.resizeListener =
			new QuickParameterListenerThread(new IntegerArrayGetter() {
				@Override
				public int[] getIntegerArray() {
					return new int[]{ comp.getWidth(), comp.getHeight() };
				}
			}, new Runnable() {
				@Override
				public void run() {
					onComponentResized(lastResizeEvent);
				}
			}, tick);
		
		this.moveListener =
			new QuickParameterListenerThread(new IntegerArrayGetter() {
				@Override
				public int[] getIntegerArray() {
					Point location = comp.getLocation();
					return new int[]{ location.x, location.y };
				}
			}, new Runnable() {
				@Override
				public void run() {
					onComponentMoved(lastMoveEvent);
				}
			}, tick);
	}
	
	public ExtendedComponentListener(Component component) {
		this(component, QuickParameterListenerThread.DEFAULT_TICK);
	}
	
	@Override
	public final void componentResized(ComponentEvent e) {
		onComponentResizing(e);
		resizeListener.startListening();
	}

	@Override
	public final void componentMoved(ComponentEvent e) {
		onComponentMoving(e);
		moveListener.startListening();
	}
	
	public boolean isListening() {
		return resizeListener.isIterating() || moveListener.isIterating();
	}
	
	//
	
	public abstract void onComponentResizing(ComponentEvent e);
	/**
	 * Tells the listener that component is not resizing at least for <code>TICK</code> ms.
	 */
	public abstract void onComponentResized(ComponentEvent e);
	
	public abstract void onComponentMoving(ComponentEvent e);
	/**
	 * Tells the listener that component is not moving at least for <code>TICK</code> ms.
	 */
	public abstract void onComponentMoved(ComponentEvent e);
}
