angular.module('odontoFacil').controller('dashboardController', ['$scope', '$rootScope', '$http', '$location', 
	'$mdDialog', 'funcionarioFactory' , 'Session', 'homeFactory', 'agendamentoFactory',	function($scope, $rootScope, 
			$http, $location, $mdDialog, funcionarioFactory, Session, homeFactory, agendamentoFactory) {
	
	var ctrl = this;
	ctrl.funcionario = {};
	ctrl.x = Session.usuario;
//	ctrl.agendamentosMes = {};

	
//	ctrl.agendamentosDoMes = function() {
//		agendamentoFactory.agendamentosDoMes().then(function successCallback(response) {
//			ctrl.agendamentosMes = response.data;
//		}, function errorCallback(response) {
//			console.log(response.data);
//			console.log(response.status);
//		});
//	}
	
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
			  url: 'https://localhost:8443/userLogado/'
			}).then(function successCallback(response) {
			    Session.create(response.data);
			    ctrl.x = response.data;
			  }, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			  });
		
	}
//	ctrl.agendamentosDoMes();
	ctrl.funcionarioLogado();
}]);