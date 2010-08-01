package org.vaadin.addons.lazyquerycontainer;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

public class QueryItemStatusColumnGenerator implements ColumnGenerator, ValueChangeListener {
	private static final long serialVersionUID = 1L;

	private Application application;
	private Resource noneIconResource;
	private Resource addedIconResource;
	private Resource modifiedIconResource;
	private Resource removedIconResource;
	private Embedded statusIcon;
	
	public QueryItemStatusColumnGenerator(Application application) {
		this.application=application;
	}
	
	@Override
	public Component generateCell(Table source, Object itemId, Object columnId) {
		Property statusProperty = source.getItem(itemId).getItemProperty(columnId);

		noneIconResource = new ClassResource("images/textfield.png", this.application);		
		addedIconResource = new ClassResource("images/textfield_add.png", this.application);		
		modifiedIconResource = new ClassResource("images/textfield_rename.png", this.application);		
		removedIconResource = new ClassResource("images/textfield_delete.png", this.application);		
		
		statusIcon=new Embedded(null,noneIconResource);
		statusIcon.setHeight("16px");

		if(statusProperty instanceof ValueChangeNotifier) {
			ValueChangeNotifier notifier=(ValueChangeNotifier) statusProperty;
			notifier.addListener(this);
		}

		refreshImage(statusProperty);
		
		return statusIcon;
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		refreshImage(event.getProperty());	
		statusIcon.requestRepaint();
	}
	
	public void refreshImage(Property statusProperty) {
		if(statusProperty.getValue()==null) {
			statusIcon.setSource(noneIconResource);
			return;
		}	
		QueryItemStatus status=(QueryItemStatus)statusProperty.getValue();
		if(status==QueryItemStatus.None) {
			statusIcon.setSource(noneIconResource);
		}
		if(status==QueryItemStatus.Modified) {
			statusIcon.setSource(modifiedIconResource);
		}
		if(status==QueryItemStatus.Added) {
			statusIcon.setSource(addedIconResource);
		}
		if(status==QueryItemStatus.Removed) {
			statusIcon.setSource(removedIconResource);
		}				
	}

}
