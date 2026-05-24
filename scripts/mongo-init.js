// 1. Crear usuario primero (en admin)
db = db.getSiblingDB('admin');
db.createUser({
  user: "persona_db",
  pwd: "persona_db",
  roles: [
    { role: "read",      db: "persona_db" },
    { role: "readWrite", db: "persona_db" },
    { role: "dbAdmin",   db: "persona_db" }
  ],
  mechanisms: ["SCRAM-SHA-1", "SCRAM-SHA-256"]
});

// 2. Cambiar a persona_db e insertar datos
db = db.getSiblingDB('persona_db');

db.persona.insertMany([
  {
    "_id": NumberInt(123456789),
    "nombre": "Pepe",
    "apellido": "Perez",
    "genero": "M",
    "edad": NumberInt(30),
    "_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
  },
  {
    "_id": NumberInt(987654321),
    "nombre": "Pepito",
    "apellido": "Perez",
    "genero": "M",
    "_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
  },
  {
    "_id": NumberInt(321654987),
    "nombre": "Pepa",
    "apellido": "Juarez",
    "genero": "F",
    "edad": NumberInt(30),
    "_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
  },
  {
    "_id": NumberInt(147258369),
    "nombre": "Pepita",
    "apellido": "Juarez",
    "genero": "F",
    "edad": NumberInt(10),
    "_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
  },
  {
    "_id": NumberInt(963852741),
    "nombre": "Fede",
    "apellido": "Perez",
    "genero": "M",
    "edad": NumberInt(18),
    "_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
  }
], { ordered: false });

print("MongoDB init: usuario persona_db creado y datos insertados correctamente");
