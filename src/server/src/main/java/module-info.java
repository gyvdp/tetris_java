module tetris.server {
  requires tetris.model;
  requires tetris.message;
  requires tetris.database;
  requires java.desktop;
  requires java.logging;

  exports esi.acgt.atlj.server;
}