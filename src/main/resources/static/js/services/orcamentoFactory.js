angular.module('odontoFacil').factory('consultaFactory',['$http', function($http) {
	
	var _orcamento;
	var _editandoorcamento;
	
	var _listarorcamentos = function() {
		return $http.get(
			'https://localhost:8443/listarorcamentos'
		);
	};
	
	var _salvarorcamentos = function(orcamento) {
		return $http.post(
			'https://localhost:8443/salvarorcamentos',
			orcamento
		);
	};
	
	var _excluirorcamentos = function(orcamento) {
		return $http.post(
			'https://localhost:8443/excluirorcamentos/',
			orcamento
		);
	};
	
	return {
		getorcamento: function() { return _orcamento; },
		setorcamento: function(orcamento) { _orcamento = orcamento; },
		isEditandoorcamento: function() { return _editandoorcamento; },
		setEditandoorcamento: function(editandoorcamento) { _editandoorcamento = editandoorcamento; },
		listarorcamentos: _listarorcamentos,
		salvarorcamentos: _salvarorcamentos,
		excluirorcamentos: _excluirorcamentos
	}
}]);