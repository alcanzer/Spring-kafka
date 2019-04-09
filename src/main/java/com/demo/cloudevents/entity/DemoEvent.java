package com.demo.cloudevents.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter

/***
 * Demo object to create complex JSON in CloudEvents
 * @author Joseph
 *
 */
public class DemoEvent {
	private String key;
	private String value;
}
