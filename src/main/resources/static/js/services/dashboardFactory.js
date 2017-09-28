angular.module('odontoFacil').factory('dashboardFactory',['$http', function($http) {
	var _funcionario;
	
	var ctrl = this;
	
	
	
	return {
		getFuncionario: function() { return _funcionario; },
		setFuncionario: function(funcionario) { _funcionario = funcionario; },
		listarPermissoes: _listarPermissoes,
	}
}]);