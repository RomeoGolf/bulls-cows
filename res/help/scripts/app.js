var app1 = angular.module('app1', ['ngRoute']);

    app1.config(function($routeProvider){
        $routeProvider.when('/about_game',
        {
			templateUrl:'views/about_game.html',
        });
        $routeProvider.when('/about_game_3',
        {
			templateUrl:'/views/about_game.html',
        });
        $routeProvider.when('/algorythm',
        {
			templateUrl:'views/about_algorythm.html',
        });
        $routeProvider.when('/program',
        {
			templateUrl:'views/work_with_program.html',
        });

        $routeProvider.when('/about',
        {
			templateUrl:'views/about.html',
        });

	$routeProvider.otherwise({redirectTo: '/about_game'});

});

app1.run(function($rootScope, $location, $anchorScroll, $routeParams) {
  $rootScope.$on('$routeChangeSuccess', function(newRoute, oldRoute) {
    $location.hash($routeParams.scrollTo);
    $anchorScroll();  
  });
});


