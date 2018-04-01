package ru.spark.slauncher.managers;

import ru.spark.slauncher.SLauncher;
import ru.spark.slauncher.component.ComponentDependence;
import ru.spark.slauncher.component.InterruptibleComponent;
import ru.spark.slauncher.component.LauncherComponent;
import ru.spark.slauncher.component.RefreshableComponent;
import ru.spark.util.async.LoopedThread;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentManager {
	private final SLauncher tlauncher;

	private final List<LauncherComponent> components;
	private final ComponentManagerRefresher refresher;

	public ComponentManager(SLauncher tlauncher) {
		if (tlauncher == null)
			throw new NullPointerException();

		this.tlauncher = tlauncher;
		this.components = Collections
				.synchronizedList(new ArrayList<LauncherComponent>());

		this.refresher = new ComponentManagerRefresher(this);
		this.refresher.start();
	}

	public SLauncher getLauncher() {
		return tlauncher;
	}

	public <T extends LauncherComponent> T loadComponent(Class<T> classOfT) {
		if (classOfT == null)
			throw new NullPointerException();

		if (hasComponent(classOfT))
			return getComponent(classOfT);

		ComponentDependence dependence = classOfT
				.getAnnotation(ComponentDependence.class);
		if (dependence != null)
			for (Class<?> requiredClass : dependence.value())
				rawLoadComponent(requiredClass);

		return rawLoadComponent(classOfT);
	}

	private <T> T rawLoadComponent(Class<T> classOfT) {
		if (classOfT == null)
			throw new NullPointerException();

		if (!LauncherComponent.class.isAssignableFrom(classOfT))
			throw new IllegalArgumentException(
					"Given class is not a LauncherComponent: "
							+ classOfT.getSimpleName());

		Constructor<T> constructor;

		try {
			constructor = classOfT.getConstructor(ComponentManager.class);
		} catch (Exception e) {
			throw new IllegalStateException(
					"Cannot get constructor for component: "
							+ classOfT.getSimpleName(), e);
		}

		T instance;

		try {
			instance = constructor.newInstance(this);
		} catch (Exception e) {
			throw new IllegalStateException(
					"Cannot create a new instance for component: "
							+ classOfT.getSimpleName(), e);
		}

		LauncherComponent component = (LauncherComponent) instance;
		components.add(component);

		return instance;
	}

	@SuppressWarnings("unchecked")
	public <T extends LauncherComponent> T getComponent(Class<T> classOfT) {
		if (classOfT == null)
			throw new NullPointerException();

		for (LauncherComponent component : components)
			if (classOfT.isInstance(component))
				return (T) component;

		throw new IllegalArgumentException("Cannot find the component!");
	}

	<T extends LauncherComponent> boolean hasComponent(Class<T> classOfT) {
		if (classOfT == null)
			return false;

		for (LauncherComponent component : components)
			if (classOfT.isInstance(component))
				return true;

		return false;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getComponentsOf(Class<T> classOfE) {
		List<T> list = new ArrayList<T>();

		if (classOfE == null)
			return list;

		for (LauncherComponent component : components)
			if (classOfE.isInstance(component))
				list.add((T) component);

		return list;
	}

	public void startAsyncRefresh() {
		refresher.iterate();
	}

	void startRefresh() {
		for (LauncherComponent component : components)
			if (component instanceof RefreshableComponent) {
				RefreshableComponent interruptible = (RefreshableComponent) component;
				interruptible.refreshComponent();
			}
	}

	public void stopRefresh() {
		for (LauncherComponent component : components)
			if (component instanceof InterruptibleComponent) {
				InterruptibleComponent interruptible = (InterruptibleComponent) component;
				interruptible.stopRefresh();
			}
	}

	class ComponentManagerRefresher extends LoopedThread {
		private final ComponentManager parent;

		ComponentManagerRefresher(ComponentManager manager) {
			super("ComponentManagerRefresher");
			this.parent = manager;
		}

		@Override
		protected void iterateOnce() {
			parent.startRefresh();
		}
	}
}
