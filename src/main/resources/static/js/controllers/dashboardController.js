angular.module('odontoFacil').controller('dashboardController', ['$scope', '$rootScope', '$http', '$location', 
	'$mdDialog', 'funcionarioFactory' , 'Session', 'homeFactory',	function($scope, $rootScope, 
			$http, $location, $mdDialog, funcionarioFactory, Session, homeFactory) {
	
	var ctrl = this;
	ctrl.funcionario = {};
	ctrl.x = Session.usuario;

	
	ctrl.funcionarioLogado = function(x) {
		/*homeFactory.funcionarioLogado(nomeFuncionario).then(function successCallback(response){
		Session.create(response.data);
		ctrl.funcionario = response.data;
		return ctrl.funcionario;
	}, function errorCallback(response) {
		console.log(response.data);
		console.log(response.status);
	});*/
		$http({
			  method: 'GET',
			  url: 'http://localhost:8080/userLogado/'
			}).then(function successCallback(response) {
			    Session.create(response.data);
			    ctrl.x = response.data;
			  }, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			  });
		
	}
	ctrl.funcionarioLogado();
}]);