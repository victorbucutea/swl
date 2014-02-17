/**
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
 * 
 */
define([], function() {
	"use strict";
	return function($rootScope, $injector) {
        var buffer = new Array();
        
		return {

			retryHttpRequest : function retryHttpRequest(config, deferred) {
				function successCallback(response) {
					deferred.resolve(response);
				}
				function errorCallback(response) {
					deferred.reject(response);
				}
				var $http = $injector.get('$http');
				$http(config).then(successCallback, errorCallback);
			},

			/**
			 * Retries all the buffered requests clears the buffer.
			 */
			retryAll : function() {
				for ( var i = 0; i < buffer.length; ++i) {
					this.retryHttpRequest(buffer[i].config, buffer[i].deferred);
				}
				buffer = [];
			},

			/**
			 * Appends HTTP request configuration object with deferred response
			 * attached to buffer.
			 */
			append : function(config, deferred) {
				buffer.add({
					config : config,
					deferred : deferred
				});
			}
		};
	};
});