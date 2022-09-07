package com.qa.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.qa.util.commanUtils;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.response.Response;

public class OppenheimerTest extends commanUtils {

	@BeforeMethod()

	public void beforeMethod(Object[] param, Object[] param1) {
		test = report.startTest(param[0].toString(), param[1].toString());

	}

	// Sample set of users to be created
	org.json.JSONArray retriveData = new org.json.JSONArray();
	org.json.JSONArray userData = new org.json.JSONArray(
			"[{\"birthday\": \"07071994\",\"gender\": \"M\", \"name\": \"Google.S1\", \"natid\": \"NPHCG$$$$$\", \"salary\": \"700.00\", \"tax\": \"20.00\"},{\"birthday\": \"07071994\",\"gender\": \"F\", \"name\": \"Google.M1\", \"natid\": \"NPHCG$$$$$\", \"salary\": \"100.00\", \"tax\": \"52.00\"},{\"birthday\": \"07071994\",\"gender\": \"M\", \"name\": \"Google.M2\", \"natid\": \"NPHCG$$$$$\", \"salary\": \"700.00\", \"tax\": \"20.00\"}]");
	org.json.JSONArray createdData = new org.json.JSONArray();
	OppenheimerAPIWrapper apiUtils = new OppenheimerAPIWrapper();

	@Test(dataProvider = "UseCase1", priority = 0)
	public void insertSingleRecord_API(String TestCaseID, String TestCaseDescription, String BaseURI,
			String ContextPath, String PayloadData, String ResponseCode, String ExpectedResult) throws ParseException {
		try {
			System.out.println("TestCaseID+++++++++++" + TestCaseID);
			JSONParser parser = new JSONParser();
			JSONObject json = null;
			json = (JSONObject) parser.parse(PayloadData);
			Map<String, String> defaultHeaders = new HashMap<String, String>();
			defaultHeaders.put("Content-Type", "application/json");
			Response response = postCall(BaseURI, ContextPath, json, defaultHeaders);
			int actualResponseCode = response.getStatusCode();

			if (actualResponseCode == Integer.parseInt(ResponseCode)) {
				test.log(LogStatus.PASS, "The expected response Code " + ResponseCode
						+ " matched the actual response code " + response.statusCode());
			} else {
				test.log(LogStatus.FAIL, "The expected response Code " + ResponseCode
						+ " not matched the actual response code " + response.statusCode());
			}
		} catch (NumberFormatException e) {
			test.log(LogStatus.ERROR, "insertSingleRecord_API_Error1" + e.getMessage());
		} catch (ParseException e) {
			test.log(LogStatus.ERROR, "insertSingleRecord_API_Error2" + e.getMessage());
		}
	}

	@Test(dataProvider = "UseCase2", priority = 1)
	public void insertMultipleRecord_API(String TestCaseID, String TestCaseDescription, String BaseURI,
			String ContextPath, String PayloadData, String ResponseCode, String ExpectedResult) throws ParseException {
		try {
			System.out.println("TestCaseID+++++++++++" + TestCaseID);
			JSONParser parser = new JSONParser();
			JSONArray json = null;
			json = (JSONArray) parser.parse(PayloadData);
			Map<String, String> defaultHeaders = new HashMap<String, String>();
			defaultHeaders.put("Content-Type", "application/json");
			Response response = postCallwithArray(BaseURI, ContextPath, json, defaultHeaders);
			int actualResponseCode = response.getStatusCode();
			if (actualResponseCode == Integer.parseInt(ResponseCode)) {
				createdData.put(json);
				test.log(LogStatus.PASS, "The expected response Code " + ResponseCode
						+ " matched the actual response code " + response.statusCode());
			} else {
				test.log(LogStatus.FAIL, "The expected response Code " + ResponseCode
						+ " not matched the actual response code " + response.statusCode());
			}
		} catch (NumberFormatException e) {

			test.log(LogStatus.ERROR, "insertMultipleRecord_API_Error1" + e.getMessage());
		} catch (ParseException e) {

			test.log(LogStatus.ERROR, "insertMultipleRecord_API_Error2" + e.getMessage());

		}

	}

	@Test(dataProvider = "UseCase3", priority = 2)
	public void uploadSingleRecordIntoDB_UI1(String TestCaseID, String BrowserType, String appURL, String filePath,
			String FileName, String Result) throws InterruptedException, IOException {
		System.out.println(
				"+++++++++++++++++++++++++++++TestCaseID+++++++++++" + TestCaseID + "++++++++++++++++++++++++++++");
		try {
			initiazeBrowser(BrowserType);
			driver.get(appURL);
			clickUsingAction(By.xpath(envConfig.getProperty("uploadFile_XPath")));
			Thread.sleep(1000);
			if (filePath.contains("SingleRecord")) {
				String currentDirectory = System.getProperty("user.dir");
				System.out.println("The current working directory is " + currentDirectory);
				String fileUploadPath = currentDirectory + filePath + FileName;
				System.out.println("The current single record File directory path is " + fileUploadPath);
				performUploadFileAction(fileUploadPath);
				test.log(LogStatus.PASS, "Single Record File Location ::" + fileUploadPath);
				test.log(LogStatus.PASS, "Upload Single Record " + test.addScreenCapture((capture(driver))));
			} else {
				String currentDirectory = System.getProperty("user.dir");
				System.out.println("The current working directory is " + currentDirectory);
				String getFilePath = currentDirectory + filePath + FileName;
				System.out.println("The current Multiple record File directory path is " + getFilePath);
				performUploadFileAction(getFilePath);
				test.log(LogStatus.PASS, "Multiple Record File Location ::" + getFilePath);
				test.log(LogStatus.PASS, "Upload Multiple Record " + test.addScreenCapture((capture(driver))));
			}
		} catch (InterruptedException | IOException e) {
			test.log(LogStatus.ERROR, "uploadSingleRecordIntoDB_UI1_Error1" + e.getMessage());
		} finally {
			driver.quit();
		}

	}

	@Test(dataProvider = "UseCase4", priority = 4)
	public void BookKeeping_Manager_API(String TestCaseID, String TestCaseDescription, String BaseURI,
			String ContextPath, String PayloadData, String ResponseCode, String ExpectedResult) throws ParseException {

		Response response;
		int actualResponseCode;
		try {
			System.out.println(
					"+++++++++++++++++++++++++++++TestCaseID+++++++++++" + TestCaseID + "++++++++++++++++++++++++++++");
			System.out.println("%%%%%%%%%%%" + userData.length());
			/************ AC 1 get list of created items ***************/
			test.log(LogStatus.INFO, "-------------------------  Test Case AC1 ------------------------------ ");
			response = getCall(BaseURI, ContextPath);
			System.out.println(response.getBody().asString());
			actualResponseCode = response.getStatusCode();
			retriveData = new org.json.JSONArray(response.asString());
			if (actualResponseCode == Integer.parseInt(ResponseCode)) {
				test.log(LogStatus.PASS, "The expected response Code " + ResponseCode
						+ " matched the actual response code " + response.statusCode());
			} else {
				test.log(LogStatus.FAIL, "The expected response Code " + ResponseCode
						+ " not matched the actual response code " + response.statusCode());
			}
			/********* AC 2 verify natid masking ****************/
			/** Retrieving natid from the GET call results **/
			List<String> natids = new ArrayList<String>();
			for (int i = 0; i < retriveData.length(); i++) {
				natids.add(retriveData.getJSONObject(i).getString("natid"));
			}
			
			test.log(LogStatus.INFO, "-------------------------  Test Case AC2 ------------------------------ ");
			for (int i = 0; i < natids.size(); i++) {
			
				boolean result = apiUtils.checkMask(natids.get(i));
				if (result)

					test.log(LogStatus.PASS, "The nat id only contains $ from 5th digit " + natids.get(i));
				else
					test.log(LogStatus.FAIL, "The nat id does not contains $ from 5th digit " + natids.get(i));
			}
			/** Retrieving the user salaries and taxes paid for tax calculation purpose **/
			test.log(LogStatus.INFO,
					"-------------------------  Test Case AC3, AC4, AC5, AC6 ------------------------------ ");
			for (int i = 0; i < userData.length(); i++) {

				/********* AC 3 tax relief calculation ****************/
				double taxRelief = 0;
				try {

					String genderStr = userData.getJSONObject(i).getString("gender");
					String salaryStr = userData.getJSONObject(i).getString("salary");
					String birthdayStr = userData.getJSONObject(i).getString("birthday");
					String nameStr = userData.getJSONObject(i).getString("name");
					String taxStr = userData.getJSONObject(i).getString("tax");

					test.log(LogStatus.INFO,
							"$$$$$$$$$$$$$$$$$$$$$$$$  .... Tax data for User  ....  $$$$$$$$$$$$$$$$$$$$$$$$  ->  "
									+ nameStr);
					taxRelief = apiUtils.verifyTaxRelief(Double.valueOf(salaryStr), Double.valueOf(taxStr), birthdayStr,
							genderStr);

					System.out.println(".......... Tax calculated result ..........." + taxRelief);
					test.log(LogStatus.PASS, "   .... AC3 Tax Relief calculated Result ....  >>  " + taxRelief);

				} catch (java.text.ParseException e) {

					test.log(LogStatus.ERROR, "BookKeeping_Manager_01" + e.getMessage());
				}
				/********* AC 4 Round off calculation ****************/
				test.log(LogStatus.PASS,
						"  ....AC4 Tax  Relief Round off calculation....  >>  " + Math.round(taxRelief));
				/********* AC 5 Final tax calculation ****************/
				double finalTax = apiUtils.getFinalTaxReliefAmt(taxRelief);
				System.out.println(".......... Tax Relief calculated result ..........." + taxRelief);
				test.log(LogStatus.PASS, "   .... AC5 Final tax calculation .... >>  " + finalTax);
				/********* AC 6 2 digit round round off value ****************/
				test.log(LogStatus.PASS, " ....  AC6 Final Tax Relief with 2 digit round off ....  >>  "
						+ apiUtils.taxReliefAmtTwoDecimalPoint(taxRelief));
				test.log(LogStatus.INFO, " !!!!!!!!!!!!!!!!!!!!! .... End of user data .... !!!!!!!!!!  - > ");
			}
			System.out.println("+++++++++++Result+__________" + ExpectedResult);
			test.log(LogStatus.PASS, "I am inside UseCase4");

			actualResponseCode = response.getStatusCode();
			if (actualResponseCode == Integer.parseInt(ResponseCode))
				test.log(LogStatus.PASS, "The expected response Code " + ResponseCode
						+ " matched the actual response code " + response.statusCode());
			else {
				test.log(LogStatus.FAIL, "The expected response Code " + ResponseCode
						+ " not matched the actual response code " + response.statusCode());
			}
		} catch (NumberFormatException e) {
			test.log(LogStatus.ERROR, "BookKeeping_Manager_02" + e.getMessage());
		}

	}

//UC 5
	@Test(dataProvider = "UseCase5", priority = 5)
	public void dispense_TaxRelief_Governet_UI(String TestCaseID, String TestCaseDescription, String BrowserType,
			String appURL, String ExpectedResult) throws InterruptedException {

		try {
			initiazeBrowser(BrowserType);
			driver.get(appURL);
			// :AC1: The button on the screen must be red-colored
			if (TestCaseID.contains("OP_SIT_UC5_Test_01")) {
				String dispenseNowButtonColor = getCssValue(By.xpath(envConfig.getProperty("dispenseNowButton_Xpath")),
						"background-color");
				System.out.println("storeSignButtonRBGValue" + dispenseNowButtonColor);
				if (dispenseNowButtonColor.equals(ExpectedResult)) {
					scrollDown();
					Thread.sleep(1000);
					test.log(LogStatus.PASS, "Navigated to  OPPENHEIMER App - Dispense Section "
							+ test.addScreenCapture((capture(driver))));
					test.log(LogStatus.PASS, "The expected DispenseNow Button Color-> " + ExpectedResult
							+ "<- matched with Actual Button Red-Color-> " + dispenseNowButtonColor);
				} else {
					test.log(LogStatus.FAIL, "The expected DispenseNow Button Color " + ExpectedResult
							+ "not matched with Actual Button Red-Color" + dispenseNowButtonColor);
				}
			}
			// AC2
			else if (TestCaseID.contains("OP_SIT_UC5_Test_02")) {
				String buttonText = getText(By.xpath(envConfig.getProperty("buttonText_Xpath")));
				if (buttonText.equals(ExpectedResult)) {
					scrollDown();
					Thread.sleep(1000);
					test.log(LogStatus.PASS, "Navigated to  OPPENHEIMER App - Dispense Section "
							+ test.addScreenCapture((capture(driver))));
					test.log(LogStatus.PASS, "The expected DispenseNow Button  " + ExpectedResult
							+ " matched with Actual DispenseNow Button  " + buttonText);
				} else {
					test.log(LogStatus.FAIL, "The expected DispenseNow Button  " + ExpectedResult
							+ "does not matched with Actual DispenseNow Button" + buttonText);
				}
			}
			// Cash dispensed - AC3
			else if (TestCaseID.contains("OP_SIT_UC5_Test_03")) {
				click(By.xpath(envConfig.getProperty("buttonText_Xpath")));
				String dispenseText = getText(By.xpath(envConfig.getProperty("dispenseText")));
				if (dispenseText.equals(ExpectedResult)) {
					test.log(LogStatus.PASS, "The expected Dispensed Text-> " + ExpectedResult
							+ "<-matched with Actual Dispensed Text-> " + dispenseText);
				} else {
					test.log(LogStatus.FAIL, "The expected Dispensed Text-> " + ExpectedResult
							+ "<- does not matched with Actual Button Red-Color-> " + dispenseText);
				}

				if (getURL().contains("dispense")) {
					test.log(LogStatus.PASS, "Redirected URL " + test.addScreenCapture((capture(driver))));
				} else {
					test.log(LogStatus.FAIL, "Failed to Redirect the URL");
				}
			} else {
				test.log(LogStatus.INFO, "Test Condition failed");
			}
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			test.log(LogStatus.ERROR, "dispense_TaxRelief_Governet_UI_Error1" + e.getMessage());
		} finally {
			driver.quit();
		}
	}

}
