package com.ibm.gil.customization.features;

import java.util.Locale;

import com.ibm.ecm.extension.PluginFeature;

public class ExtendedWorkPane extends PluginFeature {

	@Override
	public String getContentClass() {
		return "gilCnPluginDojo.ExtendedWorkPane";
	}

	@Override
	public String getDescription(Locale arg0) {
		return "A work view that displays results broken down by item type.";
	}

	@Override
	public String getFeatureIconTooltipText(Locale arg0) {
		return "Open Worklists";
	}

	@Override
	public String getIconUrl() {
		return "workLaunchIcon";
	}

	@Override
	public String getId() {
		return "extendedWorkView";
	}

	@Override
	public String getName(Locale arg0) {
		return "Worklists";
	}

	@Override
	public String getPopupWindowClass() {
		return null;
	}

	@Override
	public String getPopupWindowTooltipText(Locale arg0) {
		return null;
	}

	@Override
	public boolean isPreLoad() {
		return false;
	}

}
