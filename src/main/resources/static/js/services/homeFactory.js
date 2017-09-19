angular.module('odontoFacil').factory('homeFactory',['$http', function($http) {
	var ctrl = this;
	
	ctrl.funcionario = {};
	
	
	var _funcionarioLogado = function() {
		return $http.get(
			'http://localhost:8080/userLogado'
		);
	};
	
	return {
		funcionarioLogado: _funcionarioLogado,
	}
	
}]);