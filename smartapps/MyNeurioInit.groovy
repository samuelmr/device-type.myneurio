/**
 *  My Neurio Device
 *
 *  Copyright 2015 Yves Racine
 *  linkedIn profile: ca.linkedin.com/pub/yves-racine-m-sc-a/0/406/4b/
 *  Refer to readme file for installation instructions.
 *
 *  Code: https://github.com/yracine/device-type.myneurio.groovy
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */

import java.text.SimpleDateFormat

// for the UI
preferences {
	input("appKey", "text", title: "App Key (public)", description:
		"The application (public) key given by Neurio (no spaces)")
	input("privateKey", "text", title: "Private Key", description:
		"The private key given by Neurio (no spaces)")
	input("sensorId", "text", title: "MAC Address (optional, default=first Neurio found)", description:
		"The MAC address of your Neurio Sensor (no spaces and no ':')")
	input("trace", "text", title: "trace", description:
		"Set it to true to enable tracing (no spaces) or leave it empty (no tracing)"
	)
}
metadata {
	definition (name: "My Neurio Device", namespace: "yracine", author: "Yves Racine") {
		capability "Power Meter"
		capability "Refresh"
		capability "Polling"
		capability "Energy Meter"

		command "setAuthTokens"
		command "getCurrentUserInfo"
		command "getSampleStats"
		command "getLastLiveSamples"
		command "generateSampleStats"
        
		// metrics
        

		attribute "generationEnergy","string"
		attribute "generationPower","string"
		attribute "userid","string"
		attribute "username", "string"
		attribute "email","string"
		attribute "locationId","string"
		attribute "locationName","string"
		attribute "timezone","string"
		attribute "sensorId","string"

		attribute "consTotalInPeriod","string"
		attribute "consEnergyDay","string"
		attribute "consEnergyWeek","string"
		attribute "consEnergyMonth","string"
		attribute "consEnergy2DaysAgo","string"
		attribute "consEnergy2WeeksAgo","string"
		attribute "consEnergy2MonthsAgo","string"
		attribute "genEnergyDay","string"
		attribute "genEnergyWeek","string"
		attribute "genEnergyMonth","string"
		attribute "genEnergy2DaysAgo","string"
		attribute "genEnergy2WeeksAgo","string"
		attribute "genEnergy2MonthsAgo","string"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	// UI tile definitions
	tiles {
		valueTile(	"power","device.power",unit: 'Watts', width: 1, height: 1,canChangeIcon: false)
        	{
            		state(	"power", label:'Power ${currentValue}W',
					backgroundColor: "#ffffff"
/*
					backgroundColors: [
						[value: 500, color: "green"],
						[value: 3000, color: "orange"],
						[value: 6000, color: "red"]
                  			]
*/                            
				)
		}
		valueTile(	"energy", "device.energy", unit:'kWh',width: 1, height: 1,canChangeIcon: false
				) 
        	{
			state("energy",
					label:'Energy ${currentValue}kWh',
					backgroundColor: "#ffffff",
                 		)
		}
		valueTile(	"genPower", "device.generationPower",unit: 'Watts',width: 1, height: 1,canChangeIcon: false,
 				)
		{
			state(	"genPower", label:'GenPower ${currentValue}',
					backgroundColor: "#ffffff"
				)
		}
 		valueTile(	"genEnergy",  "device.generationEnergy", unit: 'kWh',width: 1, height: 1,canChangeIcon: false,
				) 
        	{
        		state("genEnergy",
					label:'GenEnergy ${currentValue}',
					backgroundColor: "#ffffff"
				)
		}
        
		 
		valueTile(	"consEnergyYesterday", "device.consEnergyDay", unit:'kWh',width: 1, height: 1,canChangeIcon: false,
 					decoration: "flat"
				) 
        	{
			state("default",
					label:'Yestrday ${currentValue}kWh',
					backgroundColor: "#ffffff"
				)
		}
		valueTile(	"consEnergyLastWeek", "device.consEnergyWeek", unit:'kWh',width: 1, height: 1,canChangeIcon: false,
  					decoration: "flat"
				)
        	{
			state("default",
					label:'Week ${currentValue}kWh',
					backgroundColor: "#ffffff"
				)
		}
		valueTile(	"consEnergyLastMonth", "device.consEnergyMonth", unit:'kWh',width: 1, height: 1,canChangeIcon: false,
  					decoration: "flat"
				)
        	{
			state("default",
					label:'Month ${currentValue}kWh',
					backgroundColor: "#ffffff"
				)
		}
		valueTile(	"consEnergy2DaysAgo", "device.consEnergy2DaysAgo", unit:'kWh',width: 1, height: 1,canChangeIcon: false,
 					decoration: "flat"
				) 
        	{
			state("default",
					label:'2DayAgo ${currentValue}kWh',
					backgroundColor: "#ffffff"
				)
		}
		valueTile(	"consEnergy2WeeksAgo", "device.consEnergy2WeeksAgo", unit:'kWh',width: 1, height: 1,canChangeIcon: false,
  					decoration: "flat",
				)
        	{
			state("default",
					label:'2WksAgo ${currentValue}kWh',
					backgroundColor: "#ffffff"
				)
		}
		valueTile(	"consEnergy2MonthsAgo", "device.consEnergy2MonthsAgo", unit:'kWh',width: 1, height: 1,canChangeIcon: false,
  					decoration: "flat"
				)
        	{
			state("default",
					label:'2MoAgo ${currentValue}kWh',
					backgroundColor: "#ffffff"
				)
		}
		standardTile("refresh", "device.power") {
			state "default", action:"refresh.refresh", icon:"st.secondary.refresh"
		}

		main(["power", "energy"])
		details(["power", "energy",  "refresh", "genPower", "genEnergy", "consEnergyYesterday", "consEnergy2DaysAgo", "consEnergyLastWeek",  "consEnergy2WeeksAgo",
        		"consEnergyLastMonth","consEnergy2MonthsAgo"])
	}
}

// handle commands

 

void poll() {
    
//	getCurrentUserInfo() must be called prior to poll()
	
	def sensorId= determine_sensor_id("") 	    
	getLastLiveSamples(sensorId)
    
	if (settings.trace) {
		log.debug "poll>sensorId = ${sensorId}"
		sendEvent name: "verboseTrace", value:
			"poll>sensorId = ${sensorId}"
	}

	def dataEvents = [
		userid:data?.user.id,
		username:data?.user.name,
		email:data?.user.email,
		locationName:data?.user.locations[0].name,
		timezone:data?.user.locations[0].timezone,
		sensorId:data?.user.locations[0]?.sensors[0]?.id,
		power:data?.liveSamples.consumptionPower,
		energy:data?.liveSamples.consumptionEnergy,
		generationPower:data?.liveSamples.generationPower,
		generationEnergy:data?.liveSamples.generationEnergy
	]
	generateEvent(dataEvents)

}


private void generateEvent(Map results)
{
	if (settings.trace) {
		log.debug "generateEvents>parsing data $results"
	}
    
	if(results)
	{
		results.each { name, value ->
			def isDisplayed = true

// 			Energy variable names contain "energy"           

			if ((name.toUpperCase().contains("ENERGY"))) {  
				float energyValue = getEnergy(value).toFloat().round(1)                
				def isChange = isStateChange(device, name, energyValue.toString())
				isDisplayed = isChange
                
				sendEvent(name: name, value: energyValue.toString(), unit: "kWh")                                     									 
			} else if (name.toUpperCase().contains("POWER")) { // power variable names contain 'power'

// 			Power variable names contain "power"

 				Long powerValue = value.toLong()
				def isChange = isStateChange(device, name, powerValue.toString())
				isDisplayed = isChange
				sendEvent(name: name, value: powerValue.toString(), unit: "Watts")                                     									 

 			} else {
				def isChange = isStateChange(device, name, value)
				isDisplayed = isChange

				sendEvent(name: name, value: value, isStateChange: isChange)       
			}
		}
	}
}




private def getEnergy(value) {

	// conversion from watts-sec to KWh 
	return (value/ (60*60*1000))
}

void refresh() {
	poll()
}
private def api(method, args, success={}) {
	String URI_ROOT = "${get_URI_ROOT()}"
	if (!isLoggedIn()) {
		login()
	}
	if (isTokenExpired()) {
		if (settings.trace) {
			log.debug "api> need to refresh tokens"
		}
/*
		refresh_tokens()

*/
		setAuthTokens()
	}
	def methods = [
		'currentUserInfo': 
			[uri:"${URI_ROOT}/users/current", type: 'get'],
		'lastLiveSamples': 
			[uri:"${URI_ROOT}/samples/live/last?${args}", 
          		type: 'get'],
		'sampleStats': 
			[uri:"${URI_ROOT}/samples/stats?${args}", 
          		type: 'get'],
		]
	def request = methods.getAt(method)
	if (settings.trace) {
		log.debug "api> about to call doRequest with (unencoded) args = ${args}"
		sendEvent name: "verboseTrace", value:
			"api> about to call doRequest with (unencoded) args = ${args}"
	}
	doRequest(request.uri, args, request.type, success)
}

// Need to be authenticated in before this is called. So don't call this. Call api.
private def doRequest(uri, args, type, success) {
	def params = [
		uri: uri,
		headers: [
			'Authorization': "${data.auth.token_type} ${data.auth.access_token}",
			'Content-Type': "application/json",
			'charset': "UTF-8",
			'Accept': "application/json"
		],
		body: args
	]
	try {
		if (settings.trace) {
//			sendEvent name: "verboseTrace", value: "doRequest>token= ${data.auth.access_token}"
			sendEvent name: "verboseTrace", value:
				"doRequest>about to ${type} with uri ${params.uri}, args= ${args}"
				log.debug "doRequest> ${type}> uri ${params.uri}, args= ${args}"
		}
		if (type == 'post') {
			httpPostJson(params, success) 
		} else if (type == 'get') {
			params.body = null // parameters already in the URL request
			httpGet(params, success) 
		}
	} catch (java.net.UnknownHostException e) {
		log.error "doRequest> Unknown host - check the URL " + params.uri
		sendEvent name: "verboseTrace", value: "doRequest> Unknown host"
	} catch (java.net.NoRouteToHostException e) {
		log.error "doRequest> No route to host - check the URL " + params.uri
		sendEvent name: "verboseTrace", value: "doRequest> No route to host"
	} catch (java.io.IOException e) {
		log.error "doRequest> general or malformed request error " + params.body
		sendEvent name: "verboseTrace", value:
			"doRequest> general or malformed request body error " + params.body
	}
}


void getLastLiveSamples(sensorId) {
	def NEURIO_SUCCESS=200

	sensorId= determine_sensor_id(sensorId) 	    
	def statusCode=true
	int j=0        
	def bodyReq="sensorId=" + sensorId 
    
	while ((statusCode!= NEURIO_SUCCESS) && (j++ <2)) { // retries once if api call fails

		api('lastLiveSamples', bodyReq) {resp->
		
			statusCode= resp.status
			if (statusCode == NEURIO_SUCCESS) {
				data?.liveSamples=resp.data
				def consumptionPower = data.liveSamples.consumptionPower
				def consumptionEnergy = data.liveSamples.consumptionEnergy
				def generationPower = data.liveSamples.generationPower
				def generationEnergy = data.liveSamples.generationEnergy
				def timestamp = data.liveSamples.timestamp    
				if (settings.trace) {
					log.debug "getLastLiveSamples>sensorId=${sensorId},consumptionPower=${consumptionPower},consumptionEnergy=${consumptionEnergy}" +
						",generationPower=${generationPower},generationEnergy=${generationEnergy}"
					sendEvent name: "verboseTrace", value:"getLastLiveSamples>sensorId=${sensorId},consumptionPower=${consumptionPower},consumptionEnergy=${consumptionEnergy}" +
						",generationPower=${generationPower},generationEnergy=${generationEnergy}"
				}
                
			} else {
				def message = resp.message
            			def errors = resp.errors
				log.error "getLastLiveSamples>status=${statusCode.toString()},message=${message},errors=${errors}"
				sendEvent name: "verboseTrace", value:"getLastLiveSamples>status=${statusCode.toString()},message=${message},errors=${errors}"
			}                
		}  /* end api call */              
	} /* end while */
}

def generateSampleStats(sensorId) {

// generate stats for yesterday

	Date endDate = new Date().clearTime()
	Date startDate = (endDate -1).clearTime()

	String nowInLocalTime = new Date().format("yyyy-MM-dd HH:mm", location.timeZone)
	if (settings.trace) {
		log.debug("generateSampleStats>yesterday: local date/time= ${nowInLocalTime}, startDate in UTC = ${String.format('%tF %<tT',startDate)}," +
			"endDate in UTC= ${String.format('%tF %<tT', endDate)}")
	}
	getSampleStats(sensorId, ISOdateFormat(startDate),ISOdateFormat(endDate),"days",null)
	Long totalInPeriod =  device.currentValue("consTotalInPeriod").toLong()
	def dataStats = ['consEnergyDay':totalInPeriod]    

// generate stats for 2 days ago

	endDate=startDate
	startDate = (endDate -2).clearTime()

	if (settings.trace) {
		log.debug("generateSampleStats>2 days ago: startDate in UTC = ${String.format('%tF %<tT',startDate)}," +
			"endDate in UTC= ${String.format('%tF %<tT', endDate)}")
	}
	getSampleStats(sensorId, ISOdateFormat(startDate),ISOdateFormat(endDate),"days",null)
	totalInPeriod =  device.currentValue("consTotalInPeriod").toLong()
	dataStats = dataStats + ['consEnergy2DaysAgo':totalInPeriod]    

// generate stats for the past week

	endDate = new Date().clearTime()
	startDate = (endDate -7).clearTime()

	if (settings.trace) {
		log.debug("generateSampleStats>past week (last 7 days): startDate in UTC = ${String.format('%tF %<tT',startDate)}," +
			"endDate in UTC= ${String.format('%tF %<tT', endDate)}")
	}
	getSampleStats(sensorId, ISOdateFormat(startDate),ISOdateFormat(endDate),"weeks",null)
	totalInPeriod =  device.currentValue("consTotalInPeriod").toLong()
	dataStats = dataStats + ['consEnergyWeek':totalInPeriod]    


// generate stats for 2 weeks ago

	endDate = startDate
	startDate = (endDate -7).clearTime()

	if (settings.trace) {
		log.debug("generateSampleStats>2 weeks ago: startDate in UTC = ${String.format('%tF %<tT',startDate)}," +
			"date/time endDate in UTC= ${String.format('%tF %<tT', endDate)}")
	}
	getSampleStats(sensorId, ISOdateFormat(startDate),ISOdateFormat(endDate),"weeks",null)
	totalInPeriod =  device.currentValue("consTotalInPeriod").toLong()
	dataStats = dataStats + ['consEnergy2WeeksAgo':totalInPeriod]    

// generate stats for the past month

	endDate = new Date().clearTime()
	Calendar oneMonthAgoCal = new GregorianCalendar()
	oneMonthAgoCal.add(Calendar.MONTH, -1)
	Date oneMonthAgo = oneMonthAgoCal.getTime().clearTime()
	
	if (settings.trace) {
		log.debug("generateSampleStats>past month: startDate in UTC = ${String.format('%tF %<tT',oneMonthAgo)}," +
			"endDate in UTC= ${String.format('%tF %<tT', endDate)}")
	}
	getSampleStats(sensorId, ISOdateFormat(oneMonthAgo),ISOdateFormat(endDate),"months",null)
	totalInPeriod =  device.currentValue("consTotalInPeriod").toLong()
    
	dataStats = dataStats + ['consEnergyMonth':totalInPeriod]  

// generate stats for 2 months ago

	endDate=oneMonthAgo -1
	Calendar twoMonthsAgoCal = new GregorianCalendar()
	twoMonthsAgoCal.add(Calendar.MONTH, -2)
	Date twoMonthsAgo = twoMonthsAgoCal.getTime().clearTime()
	
	if (settings.trace) {
		log.debug("generateSampleStats>2 months ago: startDate in UTC = ${String.format('%tF %<tT',twoMonthsAgo)}," +
			"endDate in UTC= ${String.format('%tF %<tT', endDate)}")
	}
	getSampleStats(sensorId, ISOdateFormat(twoMonthsAgo),ISOdateFormat(endDate),"months",null)
	totalInPeriod =  device.currentValue("consTotalInPeriod").toLong()
    
	dataStats = dataStats + ['consEnergy2MonthsAgo':totalInPeriod]  

	generateEvent(dataStats)


}

private def ISOdateFormat(date) {
 	SimpleDateFormat ISO8601format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
	def ISOdate = ISO8601format.format(date)
    
	return ISOdate
}

def getSampleStats(sensorId,start,end,granularity,frequency) {
	def NEURIO_MIN_INTERVAL=5
	def NEURIO_SUCCESS=200
    Long totalConsumedEnergy=0
    
	sensorId= determine_sensor_id(sensorId) 	    
	start = start?.trim()
	end = end?.trim()
	granularity=granularity?.trim()
	frequency = frequency?.trim()
	// make sure start and granularity are defined
	if ((!start) || (!granularity)) {
		log.error "getSamplesData>start and granularity are required parameters"
		sendEvent name: "verboseTrace", value:"getSamplesData>start and granularity are required parameters"
		return false
	}
	
	// make sure that frequency is a multiple of 5 if granularity is in minute
	if ((granularity == 'minutes') && (frequency)) {
		if ((frequency.mod(NEURIO_MIN_INTERVAL)) != 0) {
			log.error "getSamplesData>only multiples of 5 are supported for frequency (${frequency}) when granularity (${granularity}) is in minutes"
			sendEvent name: "verboseTrace", value:  "getSamplesData>only multiples of 5 are supported for frequency (${frequency}) when granularity (${granularity}) is in minutes"
			return false
		}
	}
	// make sure granularity is one of the correct values
	if ((granularity != 'minutes') && (granularity != 'hours') && (granularity != 'days') && (granularity != 'weeks') && (granularity != 'months')) {
		log.error "getSamplesData>only values of [minutes, hours, days, weeks or months] are supported for granularity (${granularity})"
		sendEvent name: "verboseTrace", value: "only values of minutes, hours, days or months are supported for granularity (${granularity})"
		return false
	}
     
	def bodyReq="sensorId=" + sensorId + "&start=" + start 
	if ((end != null) && (end != "")) {
		bodyReq = bodyReq+ "&end=" + end    	
	}
	bodyReq = bodyReq+ "&granularity=" + granularity    	
	if ((frequency != null) && (frequency != "")) {
		bodyReq = bodyReq+ "&frequency=" + frequency    	
	}
	int j=0        
	def statusCode=true
    
	while ((statusCode!= NEURIO_SUCCESS) && (j++ <2)) { // retries once if api call fails

		api('sampleStats', bodyReq) {resp->
			statusCode = resp.status        
        		if (statusCode == NEURIO_SUCCESS) {
 				data?.sampleStats=resp.data
				data?.sampleStatsCount=data.sampleStats.size()
				if (settings.trace) {
					log.debug "getSampleStats>sensorId=${sensorId},resp data = ${resp.data}" 
                
				}
				if (data.sampleStatsCount>0) {                
					for (i in 0..data.sampleStatsCount-1) {       
						Long consumptionEnergy = data.sampleStats[i].consumptionEnergy.toLong()
						totalConsumedEnergy =totalConsumedEnergy + consumptionEnergy
						def startTime = data.sampleStats[i].start
						def endTime = data.sampleStats[i].end
						if (settings.trace) {
							log.debug "getSampleStats>sensorId=${sensorId},sampleStat no ${i},consumptionEnergy=${consumptionEnergy},start=${startTime},end=${endTime}"
							sendEvent name: "verboseTrace", value: "getSampleStats>sensorId=${sensorId},sampleStat no ${i},consumptionEnergy=${consumptionEnergy},start=${startTime},end=${endTime} "
						}
                        
					} /* end for samples */
				} /* end if */                    
			} else {
				def message = resp.message
				def errors = resp.errors
				log.error "getSamplesData>status=${statusCode.toString()},message=${message},errors=${errors}"
				sendEvent name: "verboseTrace", value:"getSamplesData>status=${statusCode.toString()},message=${message},errors=${errors}"
			} /* end if statusCode */               
		}  /* end api call */              
	} /* end while */
	generateEvent([consTotalInPeriod: totalConsumedEnergy.toString()])
}


void getCurrentUserInfo() {
	def NEURIO_SUCCESS=200

	def statusCode=true
	int j=0        
	while ((statusCode != NEURIO_SUCCESS) && (j++ <2)) { // retries once if api call fails

		if (settings.trace) {
			log.debug "getCurrentUserInfo>about to call api"
		}
		api('currentUserInfo', null) {resp ->
			statusCode = resp.status        
			if (statusCode == NEURIO_SUCCESS) {
        			data?.user=resp.data
				if (settings.trace) {
					log.debug "getCurrentUserInfo>resp data = ${resp.data}" 
                
				}
				def userid = data.user.id
				def username = data.user.name
				def email = data.user.email
				def active = data.user.status  
				if (status != 'active') {
					if (settings.trace) {
						log.debug "getCurrentUserInfo>userId=${userid},name=${username},email=${email} not active, exiting..."
						sendEvent name: "verboseTrace", value: "getCurrentUserInfo>userId=${userid},name=${username},email=${email} not active, exiting..."
					}
					return
				}
                
				if (settings.trace) {
					log.debug "getCurrentUserInfo>userId=${userid},name=${username},email=${email},active=${active}"
					sendEvent name: "verboseTrace", value: "getCurrentUserInfo>userId=${userId},name=${username},email=${email},active=${active}"
				}
				data.user?.locationsCount = resp.data.locations.size()
				data.user.locations.each {
					def locationId = it.id
					def locationName = it.name
					def timezone = it.timezone
					if (settings.trace) {
						log.debug "getCurrentUserInfo> for username=${username}, found locationId=${locationId},name=${locationName},timezone=${timezone}"
						sendEvent name: "verboseTrace", value: "getCurrentUserInfo> for username=${username}, found locationId=${locationId},name=${locationName},timezone=${timezone}"
					}
					it?.sensorsCount=it.sensors.size()
					it.sensors.each {
						def sensorId = it.id
						def sensorType = it.sensorType
						if (settings.trace) {
							log.debug "getCurrentUserInfo> for location = ${locationName}, found sensorId=${sensorId},type=${sensorType}"
							sendEvent name: "verboseTrace", value: "getCurrentUserInfo>sensorId=${sensorId},type=${sensorType}"
						}
						it?.ChannelsCount=it.channels.size()
						it.channels.each {
							def channelId = it.channel
							def channelName = it.name
							def channelType = it.channelType
							if (settings.trace) {
								log.debug "getCurrentUserInfo>for sensorId=sensorId, found ChannelId=${channelId},name=${channelName},type=${channelType}"
								sendEvent name: "verboseTrace", value: "getCurrentUserInfo>ChannelId=${channelId},name=${channelName},type=${channelType}"
							}
                            
						} /* end each channel */                        
					} /* end each sensor */                        
				} /* end each location */                        
			} else {
        			statusCode=data.status
				def message = data.message
				def errors = data.errors
				log.error "getCurrentUserInfo>status=${statusCode.toString()},message=${message},errors=${errors}"
				sendEvent name: "verboseTrace", value:"getCurrentUserInfo>status=${statusCode.toString()},message=${message},errors=${errors}"
			} /* end if statusCode */                
		}  /* end api call */              
	} /* end while */
}


private def refresh_tokens() {
	def method = 
	[
		headers: [
			'Content-Type': "application/json",
			'charset': "UTF-8"
			],
		uri: "${get_URI_ROOT()}/oauth2/token?", 
		body: toQueryString([grant_type:"refresh_token",client_id:get_appKey(),client_secret:get_refresh_token()])
	]
	if (settings.trace) {
		log.debug "refresh_tokens> uri = ${method.uri}"
	}
	def successRefreshTokens = {resp ->
		if (settings.trace) {
			log.debug "refresh_tokens> response = ${resp.data}"
		}
		data.auth.access_token = resp.data.access_token
		data.auth.refresh_token = resp.data.refresh_token
		data.auth.expires_in = resp.data.expires_in
		data.auth.token_type = resp.data.token_type
		data.auth.scope = resp.data.scope
	}
	try {
		httpPostJson(method, successRefreshTokens)
	} catch (java.net.UnknownHostException e) {
		log.error "refresh_tokens> Unknown host - check the URL " + method.uri
		sendEvent name: "verboseTrace", value: "refresh_tokens> Unknown host"
		return false
	} catch (java.net.NoRouteToHostException t) {
		log.error "refresh_tokens> No route to host - check the URL " + method.uri
		sendEvent name: "verboseTrace", value: "refresh_tokens> No route to host"
		return false
	} catch (java.io.IOException e) {
		log.error "refresh_tokens> Authentication error, neurio servers cannot be reached at "
		sendEvent name: "verboseTrace", value: "refresh_tokens> Auth error"
		return false
	} catch (any) {
		log.error "refresh_tokens> general error " + method.uri
		sendEvent name: "verboseTrace", value:
			"refresh_tokens> general error at ${method.uri}"
		return false
	}
	def authexptime = new Date((now() + (data.auth.expires_in * 60 * 1000))).getTime()
	data.auth.authexptime = authexptime

	if (data.auth.sensorId) {		// Created by initalSetup, need to refresh Parent tokens and other children
		refreshParentTokens()
	}        
	if (settings.trace) {
		log.debug "refresh_tokens> expires in ${data.auth.expires_in} minutes"
		log.debug "refresh_tokens> data_auth.expires_in in time = ${authexptime}"
		sendEvent name: "verboseTrace", value:
			"refresh_tokens>expire in ${data.auth.expires_in} minutes"
	}
	return true
}

void refreshChildTokens(auth) {
	if (settings.trace) {
		log.debug "refreshChildTokens>begin token auth= $auth"
	}
	data.auth.access_token = auth.authToken
	data.auth.refresh_token = auth.refreshToken
	data.auth.expires_in = auth.expiresIn
	data.auth.token_type = auth.tokenType
	data.auth.scope = auth.scope
	data.auth.authexptime = auth.authexptime
	if (settings.trace) {
		log.debug "refreshChildTokens>end data.auth=$data.auth"
	}
}

private void refreshParentTokens() {
	if (settings.trace) {
		log.debug "refreshParentTokens>begin data.auth = ${data.auth}"
	}
	if (settings.trace) {
		log.debug "refreshParentTokens>auth=$auth, about to call parent.setParentAuthTokens"
	}         
	parent.setParentAuthTokens(data.auth)
	if (settings.trace) {
		log.debug "refreshParentTokens>end"
	}         
}

private void login() {
	if (settings.trace) {
		log.debug "login> about to call setAuthTokens"
	}
	if (data?.auth?.sensorId) {
    	// Created by initalSetup
		if (settings.trace) {
			log.debug "login> about to call refreshThisChildAuthTokens"
		}
		parent.refreshThisChildAuthTokens(this)
	} else { 
		setAuthTokens()
	}    
	if (!isLoggedIn) {
		if (settings.trace) {
			log.debug "login> no access_token..., failed"
		}
		return
	}
}



void setAuthTokens() {
	def method = 
	[
		headers: [
			'charset': "UTF-8",
			'Content-Type': "application/x-www-form-urlencoded"
			],
		uri: "${get_URI_ROOT()}/oauth2/token?",
		body: toQueryString([grant_type:"client_credentials",client_id:get_appKey(),client_secret:get_client_secret()])
	]
	def successTokens = {resp ->
		if (settings.trace) {
			log.debug "setAuthTokens> resp data= ${resp.data}"
		}
        
        
		data?.auth = resp.data
		data.auth.access_token = resp.data.access_token
		data.auth.expires_in = resp.data.expires_in
		data.auth.token_type = resp.data.token_type
		if (settings.trace) {
			log.debug "setAuthTokens> accessToken= ${data.auth.access_token}," +
				"tokenType=${data.auth.token_type}"
		}
	}
	try {
		log.debug "setAuthTokens> about to call httpPost with secret code= ${get_client_secret()}"
		httpPostJson(method, successTokens)

	} catch (java.net.UnknownHostException e) {
		log.error "setAuthTokens> Unknown host - check the URL " + method.uri
		sendEvent name: "verboseTrace", value: "setAuthTokens> Unknown host " +
			method.uri
		return
	} catch (java.net.NoRouteToHostException t) {
		log.error "setAuthTokens> No route to host - check the URL " + method.uri
		sendEvent name: "verboseTrace", value: "setAuthTokens> No route to host" +
			method.uri
		return
	} catch (java.io.IOException e) {
		log.error "setAuthTokens> Auth error, neurio servers cannot be reached at " +
			method.uri
		sendEvent name: "verboseTrace", value: "setAuthTokens> Auth error " +
			method.uri
		return
	}	        

	// determine token's expire time
	def authexptime = new Date((now() + (data.auth.expires_in * 60 * 1000))).getTime()
	data.auth.authexptime = authexptime
	if (settings.trace) {
		log.debug "setAuthTokens> expires in ${data.auth.expires_in} minutes"
		log.debug "setAuthTokens> data_auth.expires_in in time = ${authexptime}"
		sendEvent name: "verboseTrace", value:
			"setAuthTokens>expire in ${data.auth.expires_in} minutes"
	}
}
private def isLoggedIn() {
	if (data.auth == null) {
		if (settings.trace) {
			log.debug "isLoggedIn> no data auth"
		}
		return false
	} else {
		if (data.auth.access_token == null) {
			if (settings.trace) {
				log.debug "isLoggedIn> no access token"
				return false
			}
		}
	}
	return true
}
private def isTokenExpired() {
	def buffer_time_expiration=5   // set a 5 min. buffer time before token expiration to avoid auth_err 
	def time_check_for_exp = now() + (buffer_time_expiration * 60 * 1000);
	if (settings.trace) {
		log.debug "isTokenExpired> check expires_in: ${data.auth.authexptime} > time check for exp: ${time_check_for_exp}"
	}
	if (data.auth.authexptime > time_check_for_exp) {
		if (settings.trace) {
			log.debug "isTokenExpired> not expired"
		}
		return false
	}
	if (settings.trace) {
		log.debug "isTokenExpired> expired"
	}
	return true
}


// Determine id from settings or initalSetup
def determine_sensor_id(sensor_id) {
	def sensorId
    
	if ((sensor_id != null) && (sensor_id != "")) {
		sensorId = sensor_id.trim()
	} else if ((settings.sensorId != null) && (settings.sensorId  != "")) {
		sensorId = settings.sensorId.trim()
		if (settings.trace) {
			log.debug "determine_sensor_id> sensorId = ${settings.sensorId}"
		}
	} else if ((settings.sensorId == null) || (settings.sensorId  == "")) {
		settings.appKey = get_appKey() 
		settings.sensorId = data?.user?.locations[0]?.sensors[0]?.id
		sensorId=settings.sensorId
		if (settings.trace) {
			log.debug "determine_sensor_id> sensorId from data.locations= ${sensorId}"
		}
	}
	return sensorId
}

// Get the appKey for authentication
private def get_appKey() {
	return settings.appKey
}    
// Get the client secret
private def get_client_secret() {
	return settings.privateKey
}    


// Get the client's refresh token

private def get_refresh_token() {
	return data.auth.refresh_token
}    

// Called by My Neurio Init for initial creation of a child Device
void initialSetup(device_client_id, auth_data, device_tstat_id) {
	settings.trace='true'
	if (settings.trace) {
		log.debug "initialSetup>begin"
		log.debug "initialSetup> device_tstat_Id = ${device_tstat_id}"
		log.debug "initialSetup> device_client_id = ${device_client_id}"
	}	
	settings.appKey= device_client_id
	settings.sensorId = device_neurio_id

	data?.auth = settings    
	data.auth.access_token = auth_data.authToken
	data.auth.expires_in = auth_data.expiresIn
	data.auth.token_type = auth_data.tokenType
	if (settings.trace) {
		log.debug "initialSetup> settings = $settings"
		log.debug "initialSetup> data_auth = $data.auth"
		log.debug "initialSetup>end"
	}
	getCurrentUserInfo()
}

def toQueryString(Map m) {
	return m.collect { k, v -> "${k}=${URLEncoder.encode(v.toString())}" }.sort().join("&")
}

private def get_URI_ROOT() {
	return "https://api.neur.io/v1"
}

