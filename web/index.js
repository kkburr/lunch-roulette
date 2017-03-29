const Hapi = require('hapi');
const inert = require('inert');

// Create a server with a host and port
const server = new Hapi.Server();
server.register(inert);
server.connection({
  host: "0.0.0.0",
  port: process.env["WEB_SERVICE_PORT"]
});

// Add the route
server.route({
  method: 'GET',
  path: '/',
  handler: function (request, reply) {
    return reply.file('index.html');
  }
});

// Health check
server.route({
  method: 'GET',
  path: '/metrics/healthcheck',
  handler: function (request, reply) {
    const response = {
      status: 'OK',
      git_sha: process.env['GIT_SHA']
    };

    return reply(response).code(200);
  }
});

// Start the server
server.start((err) => {
  if (err) {
    throw err;
  }
  console.log('Server running at:', server.info.uri);
});