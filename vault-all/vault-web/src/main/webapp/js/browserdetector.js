var notSupportedBrowsers = [];
var BrowserDetection = {
	init: function(){
		if(notSupportedBrowsers == null || notSupportedBrowsers.length < 1){
			notSupportedBrowsers = this.defaultNotSupportedBrowsers;
		}
		this.detectBrowser();
		this.detectOS();
		if(this.browser == '' || this.browser == 'Unknown' || this.os == '' || 
				   this.os == 'Unknown' || this.browserVersion == '' || this.browserVersion == 0)
		{
			return;
		}
		var oldBrowser = false;
		for(var i = 0; i < notSupportedBrowsers.length; i++){
			if(notSupportedBrowsers[i].os == 'Any' || notSupportedBrowsers[i].os == this.os){
				if(notSupportedBrowsers[i].browser == 'Any' || notSupportedBrowsers[i].browser == this.browser){
					if(notSupportedBrowsers[i].version == "Any" || this.browserVersion <= parseFloat(notSupportedBrowsers[i].version)){
						oldBrowser = true;
						break;
					}
				} 
			}
		}
		if(oldBrowser){
			this.displayNotice();
		}
	},
	displayNotice: function(){
		document.getElementById("warning").style.display="block";
	},
	detectBrowser: function(){
		this.browser = '';
		this.browserVersion = 0;
		
		if(/Opera[\/\s](\d+\.\d+)/.test(navigator.userAgent)){
			this.browser = 'Opera';
		} else if(/MSIE (\d+\.\d+);/.test(navigator.userAgent)){
			this.browser = 'MSIE';
		} else if(/Navigator[\/\s](\d+\.\d+)/.test(navigator.userAgent)){
			this.browser = 'Netscape';
		} else if(/Chrome[\/\s](\d+\.\d+)/.test(navigator.userAgent)){
			this.browser = 'Chrome';
		} else if(/Safari[\/\s](\d+\.\d+)/.test(navigator.userAgent)){
			this.browser = 'Safari';
			/Version[\/\s](\d+\.\d+)/.test(navigator.userAgent);
			this.browserVersion = new Number(RegExp.$1);
		} else if(/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)){
			this.browser = 'Firefox';
		}
		
		if(this.browser == ''){
			this.browser = 'Unknown';
		} else if(this.browserVersion == 0) {
			this.browserVersion = parseFloat(new Number(RegExp.$1));
		}		
	},
	detectOS: function(){
		for(var i = 0; i < this.operatingSystems.length; i++){
			if(this.operatingSystems[i].searchString.indexOf(this.operatingSystems[i].subStr) != -1){
				this.os = this.operatingSystems[i].name;
				return;
			}
		}
		this.os = "Unknown";
	},
	browser: '',
	browserVersion: '',
	operatingSystems: [
           { 'searchString': navigator.platform, 'name': 'Windows', 'subStr': 'Win' },
           { 'searchString': navigator.platform, 'name': 'Mac', 'subStr': 'Mac' },
           { 'searchString': navigator.platform, 'name': 'Linux', 'subStr': 'Linux' },
           { 'searchString': navigator.userAgent, 'name': 'iPhone', 'subStr': 'iPhone/iPod' }
	],
	defaultNotSupportedBrowsers: [{'os': 'Any', 'browser': 'MSIE', 'version': 6},{'os': 'Any', 'browser': 'Firefox', 'version': 3.5},{'os': 'Any', 'browser': 'Safari', 'version': 3.9},{'os': 'Any', 'browser': 'Chrome', 'version': 16.9}]	
};