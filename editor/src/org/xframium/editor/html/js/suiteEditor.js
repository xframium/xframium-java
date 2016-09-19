var xEditor = angular.module('xEditor', [ 'selectionModel', 'ngAnimate',
		'ngSanitize', 'ui.bootstrap', 'ui.tree' ]);

xEditor
		.controller(
				'xFramiumSuiteEditor',
				function XFramiumController($scope, $interval, $http,
						$location, $uibModal, xFramiumService) {

					$scope.suiteName = "Google test suite";
					$scope.selectionContext = "";
					$scope.selectedStep;
					$scope.selectedTest;
					$scope.selectedFunction;
					$scope.selectedItem;
					$scope.selectedPage;

					$scope.currentTest;
					$scope.currentContext = '';
					$scope.currentStep;
					$scope.currentParameter;
					$scope.currentToken;
					$scope.currentElement;
					$scope.siteName;
					$scope.modelMap;
					$scope.fileManager;
					
					$scope.selectedORPage;
					$scope.selectedORElement;
					
					$scope.currentFolder;
					$scope.currentFile;
					$scope.currentFolderFiles = [];

					$scope.toggle = function(scope) {
						scope.toggle();
					}

					$scope.removeStep = function(scope) {
						scope.remove();
					}

					$scope.addTest = function() {
						var newTest = {
							"timed" : false,
							"os" : "",
							"contentKeys" : [],
							"active" : true,
							"description" : "This is a sample test case",
							"threshold" : "0",
							"stepList" : [],
							"testTags" : [],
							"linkId" : "",
							"dataDriver" : "",
							"name" : "new test",
							"inverse" : false,
							"dataProviders" : []
						}

						$scope.testList.push(newTest);
						$scope.openTest(newTest);

					}

					$scope.addFunction = function() {
						var newTest = {
							"timed" : false,
							"active" : true,
							"description" : "This is a sample function",
							"threshold" : "0",
							"stepList" : [],
							"testTags" : [],
							"linkId" : "",
							"name" : "new function",
						}

						$scope.functionList.push(newTest);
						$scope.openFunction(newTest);

					}

					$scope.cloneStep = function(stepDef) {
						if (stepDef === null || typeof (stepDef) !== 'object'
								|| 'isActiveClone' in stepDef)
							return stepDef;

						if (stepDef instanceof Date)
							var temp = new stepDef.constructor(); // or new
						// Date(obj);
						else
							var temp = stepDef.constructor();

						for ( var key in stepDef) {
							if (Object.prototype.hasOwnProperty.call(stepDef,
									key)) {
								stepDef['isActiveClone'] = null;
								temp[key] = $scope.cloneStep(stepDef[key]);
								delete stepDef['isActiveClone'];
							}
						}

						return temp;
					}

					$scope.addStep = function(stepDef) {
						if ($scope.currentTest) {
							var newStep = $scope.cloneStep(stepDef);

							newStep.pageName = 'Page';
							newStep.name = 'Element';
							$scope.currentTest.stepList.push(newStep);
							$scope.editStep(newStep);
						}
					}

					$scope.addToken = function() {
						if ($scope.currentStep) {
							var newToken = {
								"name" : "new token",
								"type" : "STATIC",
								"value" : ""
							}

							$scope.currentStep.tokenList.push(newToken);
							$scope.editToken(newToken);
						}
					}

					$scope.addTokenToParameter = function() {
						if ($scope.currentParameter) {
							var newToken = {
								"name" : "new token",
								"type" : "STATIC",
								"value" : ""
							}

							$scope.currentParameter.tokenList.push(newToken);
							$scope.editToken(newToken);
						}
					}

					$scope.addParameter = function() {
						if ($scope.currentStep) {
							var newParam = {
								"usage" : "",
								"name" : "new parameter",
								"type" : "STATIC",
								"tokenList" : [],
								"value" : ""
							}

							$scope.currentStep.parameterList.push(newParam);
							$scope.editParameter(newParam);
						}
					}

					$scope.removeParameter = function(param) {
						var tokenIndex = $scope.currentStep.parameterList
								.indexOf(param);

						if (tokenIndex >= 0) {
							$scope.currentStep.parameterList.splice(tokenIndex,
									1);
						}

					}

					$scope.removeToken = function(token) {
						var tokenIndex = $scope.currentStep.tokenList
								.indexOf(token);

						if (tokenIndex >= 0) {
							$scope.currentStep.tokenList.splice(tokenIndex, 1);
						}

					}

					$scope.removeTokenFromParam = function(token) {
						var tokenIndex = $scope.currentParameter.tokenList
								.indexOf(token);

						if (tokenIndex >= 0) {
							$scope.currentParameter.tokenList.splice(
									tokenIndex, 1);
						}

					}

					$scope.addStepToParent = function(step) {
						var stepList = findList($scope.currentTest.stepList,
								step);

						var stepIndex = stepList.indexOf(step);
						if (stepIndex > 0)
							stepList[stepIndex - 1].stepList.push(step);

						stepList.splice(stepIndex, 1);

					}

					function findList(stepList, step) {
						if (stepList.indexOf(step) >= 0) {
							return stepList;
						} else {
							for (var i = 0; i < stepList.length; i++) {
								if (stepList[i].stepList.length > 0) {
									var foundStep = findList(
											stepList[i].stepList, step);
									if (foundStep != null)
										return foundStep;
								}
							}
						}
					}

					$scope.regexValidated = 0;
					$scope.validateRegex = function() {
						try {
							var re = new RegExp($scope.currentStep.validation);
							$scope.regexValidated = 1;
						} catch (err) {
							$scope.regexValidated = 2;
						}

					}

					$scope.failureModeSelected = function(failureMode) {
						$scope.currentStep.sFailure = failureMode;
					}

					$scope.stepPageSelected = function(pageName) 
					{
						$scope.currentStep.pageName = pageName;
						$scope.currentStep.name = $scope.objectRepository[pageName].elementList[0].elementName;
						$scope.currentElement = $scope.objectRepository[pageName].elementList[0];
						$scope.selectedPage = $scope.objectRepository[pageName];

					}

					$scope.stepElementSelected = function(element) 
					{
						$scope.currentStep.name = element.elementName;
						$scope.currentElement = element;
					}
					
					$scope.pageSelected = function( page )
					{
						
						$scope.selectedORPage = page;						
						if ( $scope.selectedORPage.elementList.length > 0 )
							$scope.selectedORElement = $scope.selectedORPage.elementList[ 0 ];
						else
							$scope.selectedORElement = null;
					}
					
					$scope.addPage = function()
					{
						var newPage = {
								"className" : "",
								"pageName" : "new page",
								"elementList" : []
							}
						
						$scope.objectRepository[ newPage.pageName ] = newPage;
						$scope.pageSelected( 'new page' );
					}
					
					$scope.elementSelected = function( element )
					{
						$scope.selectedORElement = element;
					}
					
					$scope.byTypeSelected = function( by )
					{
						$scope.selectedORElement.by = by;
					}

					$scope.filterStepsBy = '';
					$scope.filterTestsBy = '';
					$scope.filterFunctionsBy = '';
					$scope.filterCurrentStepsBy = '';
					$scope.filterPagesBy = '';
					
					$scope.visible = function (key, filterPagesBy) 
					{
				        return !(filterPagesBy && filterPagesBy.length > 0
				        && key.indexOf(filterPagesBy) == -1);

				    };
				    
				    

					$scope.open = function() {
						$scope.modalInstance = $uibModal.open({
							animation : true,
							ariaLabelledBy : 'modal-title',
							ariaDescribedBy : 'modal-body',
							templateUrl : 'testDetail.html',
							scope : $scope,
							size : 'lg',

						});
					}

					$scope.editToken = function(token) {
						$scope.currentToken = token;
						if ($scope.currentToken.name == 'null')
							$scope.currentToken.name = '';
						
						$scope.modalInstance = $uibModal.open({
							animation : true,
							ariaLabelledBy : 'modal-title',
							ariaDescribedBy : 'modal-body',
							templateUrl : 'tokenDetail.html',
							scope : $scope,
							size : 'md',

						});
					}

					$scope.openObjectRepository = function( pageName, useElement ) {
						
						if ( pageName != null )
						{
							$scope.selectedORPage = $scope.objectRepository[pageName];
							$scope.selectedORElement = useElement;
						}
						else
						{
							$scope.selectedORPage = null;
							$scope.selectedORElement = null;
						}
						
						
						$scope.modalInstance = $uibModal.open({
							animation : true,
							ariaLabelledBy : 'modal-title',
							ariaDescribedBy : 'modal-body',
							templateUrl : 'objectRepository.html',
							scope : $scope,
							size : 'lg',
						});
					}
					
					$scope.editParameter = function(param) {
						$scope.currentParameter = param;
						if ($scope.currentParameter.name == 'null')
							$scope.currentParameter.name = '';
						
						$scope.paramModalInstance = $uibModal.open({
							animation : true,
							ariaLabelledBy : 'modal-title',
							ariaDescribedBy : 'modal-body',
							templateUrl : 'parameterDetail.html',
							scope : $scope,
							size : 'md',

						});
					}

					$scope.cancel = function() {
						$scope.modalInstance.close();
					};

					$scope.cancelParam = function() {
						$scope.paramModalInstance.close();
					};

					$scope.dataProviders = [
							{
								"name" : "authData",
								"fields" : [ "userName", "password" ]
							},
							{
								"name" : "addressData",
								"fields" : [ "addressOne", "addressTwo",
										"city", "state", "zipCode" ]
							} ];

					$scope.testList = [];
					$scope.functionList = [];
					$scope.supportedSteps = [];
					$scope.supportedORTypes = [];
					$scope.objectRepository = {};
					$scope.objectMap = {};


					$scope.openTest = function(test) {
						$scope.currentStep = null;
						if (test.description == 'null')
							test.description = '';
						$scope.currentTest = test;
						$scope.currentContext = 'Test';
					}

					$scope.openFunction = function(test) {
						$scope.currentStep = null;
						if (test.description == 'null')
							test.description = '';
						$scope.currentTest = test;
						$scope.currentContext = 'Function';
					}

					$scope.dataDriverSelected = function(dp) {
						if (dp == '')
							$scope.currentTest.dataDriver = 'N/A';
						else
							$scope.currentTest.dataDriver = dp.name;
					}

					$scope.addDataProvider = function(dp) {
						if ($scope.currentTest.dataProviders.indexOf(dp.name) != -1)
							return;
						$scope.currentTest.dataProviders.push(dp.name);
					}

					$scope.removeDPFromTest = function(dp) {
						if ($scope.currentTest.dataProviders.indexOf(dp.name) != -1)
							return;
						$scope.currentTest.dataProviders.splice(
								$scope.currentTest.dataProviders
										.indexOf(dp.name) - 1, 1);
					}

					$scope.tokenTypeSelected = function(token, type) {
						token.type = type;
						token.value = '';
					}
					
					$scope.usageSelected = function( parameter, type ){
						parameter.usage = type;
					}

					$scope.tabChanged = function() {
						$scope.selectionContext = '';
					}

					$scope.selectStep = function(step) {

						$scope.selectedStep = step;
						$scope.selectedItem = $scope.selectedStep;
						$scope.selectionContext = 'Step';
					}

					$scope.editStep = function(step) {
						if (step.validation == 'null')
							step.validation = '';

						if (step.poi == 'null')
							step.poi = '';

						if (step.tagNames == 'null')
							step.tagNames = '';

						if (step.context == 'null')
							step.context = '';
						
						$scope.currentStep = step;
						$scope.selectedPage = $scope.objectRepository[$scope.currentStep.pageName];
						
						for ( var i=0; i<$scope.selectedPage.elementList.length; i++ )
						{
							if ( $scope.currentStep.name == $scope.selectedPage.elementList[i].elementName )
								$scope.currentElement = $scope.selectedPage.elementList[i];
						}

					}

					$scope.openFolderBrowser = function(suiteName) {

						$scope.modalInstance = $uibModal.open({
							animation : true,
							ariaLabelledBy : 'modal-title',
							ariaDescribedBy : 'modal-body',
							templateUrl : 'folderBrowser.html',
							scope : $scope,
							size : 'md',
						});
					}
					
					$scope.openSuite = function(suiteName) {

						xFramiumService
								.openSuite( suiteName )
								.then(
										function(returnValue) {

											$scope.testList = returnValue.pageData.sC.testList;
											$scope.objectRepository = returnValue.pageData.pC.elementTree;
											$scope.objectMap = returnValue.pageData.pC.elementMap;
											$scope.functionList = returnValue.pageData.sC.functionList;
											$scope.siteName = returnValue.pageData.sC.siteName;
											$scope.modelMap = returnValue.pageData.sC.modelMap;
										});
					}
					
					$scope.getFiles = function( folderName, folderDelta )
					{
						xFramiumService
						.getFiles( folderName, folderDelta )
						.then(
								function(returnValue) {
									
									$scope.currentFile = returnValue.pageData.fileName;
									
									
									if ( $scope.currentFile != null )
									{
										$scope.cancel();
										$scope.openSuite( $scope.currentFile );
									}
									else
									{
										$scope.currentFolder = returnValue.pageData.folderName;
										$scope.currentFolderFiles = returnValue.pageData.folderList;
									}
									
								});
					}

					$scope.selectTest = function(stepIndex) {
						$scope.selectedTest = $scope.testList[stepIndex];
						if ($scope.selectedTest.description == 'null')
							$scope.selectedTest.description = '';
						$scope.selectedItem = $scope.selectedTest;
						$scope.selectionContext = 'Test';
					}

					$scope.selectFunction = function(stepIndex) {
						$scope.selectedFunction = $scope.functionList[stepIndex];
						if ($scope.selectedFunction.description == 'null')
							$scope.selectedFunction.description = '';
						$scope.selectedItem = $scope.selectedFunction;
						$scope.selectionContext = 'Function';
					}

					$scope.selectVariable = function(stepIndex) {
						$scope.selectedVariable = $scope.selectedVariables[stepIndex];
					}

					$scope.initialize = function() {
						xFramiumService
								.getSupportedSteps()
								.then(
										function(returnValue) {
											$scope.supportedSteps = returnValue.pageData;
										});
						
						xFramiumService
						.getSupportedORTypes()
						.then(
								function(returnValue) {
									$scope.supportedORTypes = returnValue.pageData;
								});
						
						xFramiumService
						.getFiles()
						.then(
								function(returnValue) {
									$scope.currentFolder = returnValue.pageData.folderName;
									$scope.currentFile = returnValue.pageData.fileName;
									$scope.currentFolderFiles = returnValue.pageData.folderList;
								});
					}

				});

xEditor.service("xFramiumService", function($http, $location) {
	return ({
		getSupportedSteps : getSupportedSteps,
		openSuite : openSuite,
		getSupportedORTypes : getSupportedORTypes,
		openPageData : openPageData,
		getFiles : getFiles
	});

	function getFiles( folderName, folderDelta ) {

		var request = null;
		if ( folderName != null )
		{
			request = $http({
				method : "get",
				url : "/folderList",
				params : { "folderName" : folderName,
					       "folderDelta" : folderDelta
				}
			});
		}
		else
		{
			request = $http({
				method : "get",
				url : "/folderList"
			});
		}
		return request.then(handleSuccess, handleError);
	};
	
	function getSupportedSteps(useId) {

		var request = $http({
			method : "get",
			url : "/steps/supported"
		});

		return request.then(handleSuccess, handleError);
	};
	
	function getSupportedORTypes(useId) {

		var request = $http({
			method : "get",
			url : "/or/supported"
		});

		return request.then(handleSuccess, handleError);
	};

	function openSuite(suiteName) {

		var request = $http({
			method : "get",
			url : "/suite/open",
			params : { "suiteName" : suiteName }
		});

		return request.then(handleSuccess, handleError);
	}
	;

	function openPageData(suiteName) {

		var request = $http({
			method : "get",
			url : "/pageData/open"

		});

		return request.then(handleSuccess, handleError);
	}
	;

	function handleError(response) {
		return null;
	}

	function handleSuccess(response) {
		if (response == undefined)
			return null;
		return response.data;
	}

});