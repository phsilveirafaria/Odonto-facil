angular.module('odontoFacil').factory('agendamentoFactory', ['$http',  function($http) {
	var _agendamento = {};
	var _agendamentoCarregado = {};	
	
	var _listarClientes = function() {
		return $http.get(
			'http://localhost:8080/listarClientes'
		);
	};
	
	var _listarFuncionarios = function() {
		return $http.get(
			'http://localhost:8080/listarFuncionarios'
		);
	};
	
	var _listarAgendamentos = function(dataInicial, dataFinal) {
		var params = {dataInicial: dataInicial.format(), dataFinal: dataFinal.format()};
		return $http.get('http://localhost:8080/listarAgendamentos', {params});
	};
	
	var _salvarAgendamento = function(ag) {
		var agendamento = angular.copy(ag);
		return $http.post('http://localhost:8080/salvarAgendamento', angular.copy(agendamento));
	};
	
	return {		
		getAgendamento: function() { return _agendamento; },
		setAgendamento: function(agendamento) { _agendamento = agendamento; },		
		getStart: function() { return _agendamento.start; },
		setStart: function(start) { _agendamento.start = start; },
		getEnd: function() { return _agendamento.end; },
		setEnd: function(end) { _agendamento.end = end; },
		listarClientes: _listarClientes,
		listarFuncionarios: _listarFuncionarios,
		getAgendamentoCarregado: function() { return _agendamentoCarregado; },
		setAgendamentoCarregado: function(agendamentoCarregado) { _agendamentoCarregado = agendamentoCarregado; },
		getEditable: function() { return _agendamento.editable; },
		setEditable: function(editable) { _agendamento.editable = editable; },
		getFormatedStart: function() { return _agendamento.formatedStart; },
		setFormatedStart: function(formatedStart) { _agendamento.formatedStart = formatedStart; },
	};
}]);