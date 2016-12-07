package com.shtitan.timesynchronize.websocket.message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.shtitan.timesynchronize.util.JsonUtils;

public class AlarmWebSocketMessage extends AbstractWebsocketMessage {
    private String sourceId;
    
    private int alarmSeverity;
    
    private String probableCause;
    
    private String alarmTime;
    
    private String isClearAlarm;

	public AlarmWebSocketMessage(String sourceId, int alarmSeverity, String probableCause, String alarmTime,
			String isClearAlarm) {
		super();
		this.sourceId = sourceId;
		this.alarmSeverity = alarmSeverity;
		this.probableCause = probableCause;
		this.alarmTime = alarmTime;
		this.isClearAlarm = isClearAlarm;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public int getAlarmSeverity() {
		return alarmSeverity;
	}

	public void setAlarmSeverity(int alarmSeverity) {
		this.alarmSeverity = alarmSeverity;
	}

	public String getProbableCause() {
		return probableCause;
	}

	public void setProbableCause(String probableCause) {
		this.probableCause = probableCause;
	}

	public String getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}

	public String getIsClearAlarm() {
		return isClearAlarm;
	}

	public void setIsClearAlarm(String isClearAlarm) {
		this.isClearAlarm = isClearAlarm;
	}
    
	public String toJsonString() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sourceId", sourceId);
		map.put("alarmSeverity", alarmSeverity);
		map.put("alarmTime", alarmTime);
		map.put("probableCause", probableCause);
		map.put("isClearAlarm", isClearAlarm);
		try {
			return JsonUtils.toJSON(map);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
    
}
