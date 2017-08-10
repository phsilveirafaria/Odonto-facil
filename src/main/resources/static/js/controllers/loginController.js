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
				$location.path('/dashboard');
			} else {											
				$rootScope.authenticated = false;
				if(response.data.name != undefined){
				$mdDialog.show(
						$mdDialog.alert()
							.clickOutsideToClose(true)
							.title('Usuario ou Senha inválido')
							.textContent('Tente novamente!')
							.ariaLabel('Alerta')
							.ok('Ok')						
					);
				}	
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