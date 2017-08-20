angular.module('odontoFacil').controller('loginController', ['$scope', '$rootScope', '$http', '$location', 
	'$mdDialog', 'loginFactory', 'funcionarioFactory',	function($scope, $rootScope, 
			$http, $location, $mdDialog, loginFactory, funcionarioFactory) {
	var ctrl = this;
		
	var authenticate = function(credentials, callback) {		
		var headers = credentials ? {authorization : "Basic "
			+ btoa(credentials.username + ":" + credentials.password)
		} : {};
		
		loginFactory.login(headers).then(function(response) {			
			if (response.data.name) {				
				$rootScope.authenticated = true;	
				//funcionarioFactory.setVinculadoGCal();				
				$location.path('/home');
			} else {											
				$rootScope.authenticated = false;
				credentials = {};	
				$scope.frmLogin.$setPristine();
			}			
			callback && callback();
		}, function() {
			$rootScope.authenticated = false;
			callback && callback();
		});
	}
	
	authenticate();	
	ctrl.credentials = {};
	ctrl.login = function() {		
		authenticate(ctrl.credentials, function() {
			if ($rootScope.authenticated) {				
				ctrl.error = false;
				
//				loginFactory.realizarBackup().then(
//						successCallback = function(response) {},
//						errorCallback = function(error) {														
//							//utilService.tratarExcecao(error);
//						}
//				);
		    } else {		    	
		    	ctrl.error = true;
				credentials = {};	
				$scope.frmLogin.$setPristine();
				
		    	$mdDialog.show(
						$mdDialog.alert()
							.clickOutsideToClose(true)
							.title('Login Inválido')
							.textContent('Usuario e senha inválidos')
							.ariaLabel('Alerta')
							.ok('Ok')						
					);	
		    }
		  });			
	};
	
	ctrl.logout = function() {
		loginFactory.logout();
		$rootScope.authenticated = false;
		ctrl.credentials = {};
	    $location.path("/");
	};
}]);