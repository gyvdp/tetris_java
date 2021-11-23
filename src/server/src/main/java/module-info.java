module atlj.server {
  requires atlj.model;
  requires atlj.message;
  requires java.desktop;
  requires java.sql;
  requires org.xerial.sqlitejdbc;

  exports esi.acgt.atlj.server;
}