angular.module('odontoFacil').controller('modalAgendamentoController', ['$scope', 
	'$location', '$mdDialog', 'agendamentoFactory', 
	function ($scope, $location, $mdDialog,	agendamentoFactory) {
	
	var ctrl = this;	
	
	agendamentoFactory.listarClientes().then(
			sucessCallback = function(response){
				ctrl.comboClientes = response.data;
			},
			errorCallback = function (error){
				
			});
	
	console.log(ctrl.comboClientes);
	ctrl.agendamento = agendamentoFactory.getAgendamento();
	
}]);