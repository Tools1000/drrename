package com.github.drrename.event;

import com.github.events1000.api.AbstractTransportingEvent;
import com.github.events1000.api.EventTopic;
import com.github.events1000.api.SynchronousEvent;
import com.github.events1000.impl.SimpleEventTopic;

import javafx.scene.control.TextField;
import net.sf.kerner.utils.pair.PairSame;

@Deprecated
public class NewFileEntryEvent extends AbstractTransportingEvent<PairSame<TextField>> implements SynchronousEvent {

	public static final EventTopic EVENT_TOPIC = new SimpleEventTopic("available-files-entry");

	public NewFileEntryEvent(final PairSame<TextField> data) {

		super(EVENT_TOPIC, data);
	}
}
