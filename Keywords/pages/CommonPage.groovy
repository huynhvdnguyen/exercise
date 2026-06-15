package pages

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import models.*

public class CommonPage {
	protected Customer customer = Customer.getInstance()
	protected Cart cart = Cart.instance
	
	protected void removeAds() {
		WebUI.executeJavaScript("""
		    document.querySelectorAll(
	            'ins.adsbygoogle,' +
	            'iframe[title="Advertisement"],' +
	            'iframe#google_esf,' +
	            'iframe[src*="doubleclick.net"],' +
	            'iframe[src*="googlesyndication.com"]'
	        ).forEach(el => el.remove());
		""", null)
	}
}
