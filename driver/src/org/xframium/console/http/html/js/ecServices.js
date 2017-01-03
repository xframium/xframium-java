xConsole.service("xConsoleService", function($http, $location) {
	return ({
		openSuite : openSuite,
		getSupportedArtifacts : getSupportedArtifacts,
		getFiles : getFiles,
		executeTest : executeTest,
		checkStatus : checkStatus
	});
	
	function checkStatus( )
    {

        var request = $http({
            method : "get",
            url : "/executionConsole/status",
        });

        return request.then(handleSuccess, handleError);
    }
	
	function executeTest( configData )
	{
	    var paramData = {};
	    for ( var key in configData )
	    {
	        paramData[ cleanString( key ) ] = cleanString( configData[ key ] );
	    }
	    
	    var request = $http({
            method : "get",
            url : "/executionConsole/executeTest",
            params: paramData
        });

        return request.then(handleSuccess, handleError);
	}
	
	function getFiles( folderName, folderDelta ) {

		var request = null;
		if ( folderName != null )
		{
			request = $http({
				method : "get",
				url : "/executionConsole/folderList",
				params : { "folderName" : folderName,
					       "folderDelta" : folderDelta
				}
			});
		}
		else
		{
			request = $http({
				method : "get",
				url : "/executionConsole/folderList"
			});
		}
		return request.then(handleSuccess, handleError);
	};
	
	
	
	function cleanString( s )
	{
	    s = s + "";
		return s.replace( new RegExp( '=', 'g' ), '_#-EQ-#_' ).replace( new RegExp( ' ', 'g' ), '_#-SP-#_' ).replace( new RegExp( '&', 'g' ), '_#-AMP-#_' );
	}
	
	
	
	function getSupportedArtifacts() {

		var request = $http({
			method : "get",
			url : "/artifacts/supported"
		});

		return request.then(handleSuccess, handleError);
	};
	
	
	function openSuite(suiteName) {

		var request = $http({
			method : "get",
			url : "/executionConsole/open",
			params : { "suiteName" : suiteName }
		});

		return request.then(handleSuccess, handleError);
	};
	
	

	function handleError(response) 
	{
		return null;
	}

	function handleSuccess(response) {
		if (response == undefined)
			return null;

		return response.data;
	}

});