angular.module('odontoFacil').controller("orcamentoController", ['orcamentoFactory', 'agendamentoFactory', 'consultaFactory', '$http', '$mdDialog', 'NgTableParams', '$scope' , '$location', 
	function(orcamentoFactory, agendamentoFactory, consultaFactory, $http, $mdDialog, NgTableParams, $scope, $location) {
	var ctrl = this;
	
	$scope.$watch(function () { return ctrl.orcamentos; }, function (newValue, oldValue) {
		ctrl.tableParams = new NgTableParams({ count: 10, sorting: { nomeCompleto: "asc" } }, { counts: [], dataset: ctrl.orcamentos });
	});
	
	// primeiro parametro, sucesso, segundo parametro erro.
	ctrl.listarOrcamentos = function() {
		orcamentoFactory.listarOrcamentos().then(function successCallback(response) {
			ctrl.orcamentos = response.data;
			console.log(response.data);
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});

	}
	
	ctrl.excluirOrcamentos = function(orcamento) {
		console.log(orcamento);
		var confirm = $mdDialog.confirm()
		.multiple(true)
		.title('Atenção')
		.textContent('Todas as informações do paciente, serão perdidas. Tem certeza que deseja continuar?')				
		.ok('Sim')
		.cancel('Não');
		
		orcamentoFactory.excluirOrcamentos(orcamento).then(function successCallback(response) {
			ctrl.listarOrcamentos();
			ctrl.orcamentos = response.data;
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});
		
	}
	
	ctrl.salvarOrcamentos = function(orcamento) {
		orcamentoFactory.salvarOrcamentos(orcamento).then(function successCallback(response) {	
			alert('orcamento cadastrado com sucesso!');
//			$mdDialog.alert(
//					$mdDialog.alert()
//						.multiple(true)
//						.clickOutsideToClose(true)
//						.title('Cadastro de orcamentos')
//						.textContent('orcamento cadastrado com sucesso!')
//						.ariaLabel('Alerta')
//						.ok('Ok')						
//				);	
			ctrl.orcamento = {};	
			ctrl.orcamentos = response.data;
			$scope.frmOrcamento.$setPristine();
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});
		
	}
	
	ctrl.listarOrcamentos();
	
}]);