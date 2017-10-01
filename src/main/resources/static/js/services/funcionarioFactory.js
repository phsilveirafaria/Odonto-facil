angular.module('odontoFacil').factory('funcionarioFactory',['$http', function($http) {
	var _funcionario;
	var _editandoFuncionario;
	
	var ctrl = this;
	
	var _listarFuncionarios = function() {
		return $http.get(
			'http://localhost:8080/listarFuncionarios'
		);
	};
	
	var _listarPermissoes = function() {
		return $http.get(
		'http://localhost:8080/listarPermissoes');
	};
	
	
	var _excluirFuncionarios = function(funcionario) {
		return $http.post(
			'http://localhost:8080/excluirFuncionarios/',
			funcionario
		);
	};
	
	var _salvarFuncionarios = function(funcionario) {
		var novoFuncionario = angular.copy(funcionario);
		
		if (novoFuncionario.cpf) {
			novoFuncionario.cpf = novoFuncionario.cpf.replace(/[^0-9]/g,'');
		}
		
		novoFuncionario.ativo = true;
		
		CredenciaisFuncionario = {
				funcionario: novoFuncionario,
				senha: novoFuncionario.senha
		}
		
		
		return $http.post(
			'http://localhost:8080/salvarFuncionarios',
			CredenciaisFuncionario
		);
	};
	
		
		
	return {
		getFuncionario: function() { return _funcionario; },
		setFuncionario: function(funcionario) { _funcionario = funcionario; },
		isEditandoFuncionario: function() { return _editandoFuncionario; },
		setEditandoFuncionario: function(editandoFuncionario) { _editandoFuncionario = editandoFuncionario; },
		listarFuncionarios: _listarFuncionarios,
		listarPermissoes: _listarPermissoes,
		salvarFuncionarios: _salvarFuncionarios,
		excluirFuncionarios: _excluirFuncionarios,
	}
}]);