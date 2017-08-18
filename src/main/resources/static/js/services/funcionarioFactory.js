angular.module('odontoFacil').factory('funcionarioFactory',['$http', function($http) {
	var _funcionario;
	var _editandoFuncionario;
	
	var _listarFuncionarios = function() {
		return $http.get(
			'http://localhost:8080/listarFuncionarios'
		);
	};
	
	var _salvarFuncionarios = function(funcionario) {
		return $http.post(
			'http://localhost:8080/salvarFuncionarios',
			funcionario
		);
	};
	
	var _excluirFuncionarios = function(funcionario) {
		return $http.post(
			'http://localhost:8080/excluirFuncionarios/',
			funcionario
		);
	};
	
	var _atualizarFuncionario = function(funcionario) {
		return $http.post('http://localhost:8080/atualizarFuncionarios', funcionario);
	};
	
	return {
		getFuncionario: function() { return funcionario; },
		setFuncionario: function(funcionario) { funcionario = funcionario; },
		isEditandoFuncionario: function() { return _editandoFuncionario; },
		setEditandoFuncionario: function(editandoFuncionario) { _editandoFuncionario = editandoFuncionario; },
		listarFuncionarios: _listarFuncionarios,
		salvarFuncionarios: _salvarFuncionarios,
		excluirFuncionarios: _excluirFuncionarios,
	}
}]);