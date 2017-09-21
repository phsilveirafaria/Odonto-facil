angular.module('odontoFacil').factory('homeFactory',['$http', function($http) {
	var ctrl = this;
	var homeFactory = {};
	ctrl.funcionario = {};
	
	
	homeFactory.funcionarioLogado = function(funcionario) {
		return $http.get(
			'http://localhost:8080/userLogado',
			funcionario
				
		);
	};
	
	return homeFactory;
	
}]);