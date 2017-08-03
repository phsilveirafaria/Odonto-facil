// criação de controllers
angular.module('odontoFacil').controller("loginController", ['loginFactory', 
	function(loginFactory, $scope, $http) {
	var ctrl = this;
	
	ctrl.cliente = {};
	
	ctrl.autenticar = function(cliente) {
		loginFactory.autenticar(cliente).then(function successCallback(response) {

		}, function errorCallback(response) {
			
		});

	}
	
}]);
