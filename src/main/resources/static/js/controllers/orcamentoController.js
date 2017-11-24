var lazyModules = ['ui.calendar', 'ui.bootstrap']; 
angular.forEach(lazyModules, function(dependency) {
	angular.module('odontoFacil').requires.push(dependency);
});

angular.module('odontoFacil').controller("orcamentoController", ['orcamentoFactory','funcionarioFactory', 'agendamentoFactory', 'consultaFactory', '$http', '$mdDialog', 'NgTableParams', '$scope' , '$location', '$uibModal', 'utilService',
	function(orcamentoFactory, funcionarioFactory, agendamentoFactory, consultaFactory, $http, $mdDialog, NgTableParams, $scope, $location, $uibModal, utilService) {
	var ctrl = this;
	ctrl.selClientes = agendamentoFactory.listarClientes();
	ctrl.selFuncionarios = agendamentoFactory.listarFuncionarios();
	ctrl.arquivo = {};
	
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
	
	/**
	 * Abre janela modal do orçamento
	 */	
	this.openEventModal = function (orcamento) {	 	
		var modalInstance = $uibModal.open({
			animation: true,
		    ariaLabelledBy: 'modal-title',
		    ariaDescribedBy: 'modal-body',
		    templateUrl: 'pages/modal_orcamento.html',
		    controller: 'orcamentoController',
		    controllerAs: 'ctrl',
		});
		
		modalInstance.result.then(function (selectedItem) {}, function () {			
			agendamentoFactory.setAgendamento({});		
		});
	};
	
	agendamentoFactory.listarClientes().then(
			sucessCallback = function(response){
				ctrl.comboClientes = response.data;
			},
			errorCallback = function (error){
				
	});
	
	funcionarioFactory.listarDentistas().then(
			sucessCallback = function(response){
				ctrl.comboFuncionarios = response.data;
			},
			errorCallback = function (error){
				
	});
	
	
	ctrl.cancel = function () {				
		$uibModalInstance.dismiss('cancel');
	}
		
	ctrl.imprimirRelatorioOrcamentos = function(orcamento) {
		utilService.setMessage("Gerando relatório ...");
		utilService.showWait();
		orcamentoFactory.imprimirRelatorioOrcamento(orcamento).then(
				successCallback = function(response) {
					utilService.hideWait();
					var file = new Blob([response.data], {
				    	type: 'application/pdf'
				    });
				    var fileURL = URL.createObjectURL(file);				    
					window.open(fileURL);							
				},
				errorCallback = function(error) {
					utilService.hideWait();
					utilService.tratarExcecao(error);
				}
		);
	};
	
	
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
	
	ctrl.salvar = function(orcamento, arquivo) {
		ctrl.salvarArquivo(arquivo);
		orcamentoFactory.salvarOrcamentos(orcamento).then(function successCallback(response) {	
			ctrl.imprimirRelatorioOrcamentos(response);
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
			$scope.orcamentoForm.$setPristine();
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});
		
	}
	
	ctrl.salvarArquivo = function(arquivo) {
		orcamentoFactory.salvarArquivo(arquivo).then(function successCallback(response) {	
			alert('arquivo cadastrado com sucesso!');
			ctrl.arquivo = {};	
			ctrl.arquivo = response.data;
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});
		
	}
	ctrl.listarOrcamentos();
	
}]);