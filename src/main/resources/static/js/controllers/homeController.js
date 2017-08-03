angular.module('odontoFacil').controller("homeController", ['homeFactory', 
	function($location) {
	var ctrl = this;
	
	$ctrl.$location = $location;

}]);

