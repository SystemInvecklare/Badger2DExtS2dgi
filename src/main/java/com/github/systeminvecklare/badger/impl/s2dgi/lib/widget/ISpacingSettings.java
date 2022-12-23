package com.github.systeminvecklare.badger.impl.s2dgi.lib.widget;

public interface ISpacingSettings {
	void setTo(int amount);
	void reset();
	ISpacingSettingOverride forIndex(int index);
	
	public interface ISpacingSettingOverride {
		void setTo(int amount);	
		void useDefault();
	} 
}
