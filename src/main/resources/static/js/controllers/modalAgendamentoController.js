angular.module('odontoFacil').controller('modalAgendamentoController', ['$scope', '$uibModalInstance', 
	'$location', '$mdDialog', 'agendamentoFactory', 
	function ($scope, $uibModalInstance, $location, $mdDialog,	agendamentoFactory) {
	
	var ctrl = this;	
	
	ctrl.comboClientes = agendamentoFactory.getListarClientes();
	ctrl.agendamento = agendamentoFactory.getAgendamento();
	
}]);