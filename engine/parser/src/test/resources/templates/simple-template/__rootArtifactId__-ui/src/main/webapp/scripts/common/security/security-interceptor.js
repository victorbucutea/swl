/**
 * Intercepts 401 & 403 responses and broadcasts the 'authRequired' message on
 * $rootScope. It is also capable of recording the response and playing it back
 * once authentication is done. This is a nice way to pop up a login screen,
 * authenticate and re-ask for the initial resource each time the server denies
 * access to a resource.
 * 
 * Many thanks to Witold Szczerba for the code below (
 * https://github.com/witoldsz/angular-http-auth )
 * 
 * 
 * @license HTTP Auth Interceptor Module for AngularJS (c) 2012 Witold Szczerba
 *          License: MIT
 * 
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

define([], function() {
	"use strict";
	return function($httpProvider) {

		$httpProvider.responseInterceptors.push([ '$rootScope', '$q', 'httpBuffer', function($rootScope, $q, httpBuffer) {


			function success(response) {
				return response;
			}

			function error(response) {
				if (response.status === 401 || response.status === 403) {
					var deferred = $q.defer();
					httpBuffer.append(response.config, deferred);
					$rootScope.$broadcast('authenticationRequired');
					return deferred.promise;
				}
				// otherwise, default behavior
				return $q.reject(response);
			}

			return function(promise) {
				return promise.then(success, error);
			};

		} ]);
	};
});