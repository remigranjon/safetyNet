package com.safetynet.alerts.repository.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alerts.model.entity.Person;
import com.safetynet.alerts.repository.interfaces.PersonRepository;
import com.safetynet.alerts.utility.JSONReaderNode;
import com.safetynet.alerts.utility.JSONWriter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PersonRepositoryJSON implements PersonRepository {
    final ObjectMapper objectMapper = new ObjectMapper();
    final JSONWriter jsonWriter;
    final JSONReaderNode jsonReader;

    public PersonRepositoryJSON(String dataFilePath) {
        jsonReader = new JSONReaderNode(dataFilePath);
        jsonWriter = new JSONWriter(dataFilePath);
    }

    private Person buildPersonFromJsonNode(JsonNode personNode) {
        return Person.builder()
                .firstName(personNode.get("firstName").asText())
                .lastName(personNode.get("lastName").asText())
                .address(personNode.get("address").asText())
                .city(personNode.get("city").asText())
                .zip(personNode.get("zip").asText())
                .phone(personNode.get("phone").asText())
                .email(personNode.get("email").asText())
                .build();
    }


    @Override
    public Set<Person> findByAddress(String address) {
        Set<Person> persons = new HashSet<>();
        final JsonNode personsNode = jsonReader.getRootNode().get("persons");
        for (JsonNode personNode : personsNode) {
            if (personNode.get("address").asText().equals(address)) {
                Person person = buildPersonFromJsonNode(personNode);
                persons.add(person);
            }
        }
        return persons;
    }

    @Override
    public Set<Person> findByLastName(String lastName) {
        Set<Person> persons = new HashSet<>();
        final JsonNode personsNode = jsonReader.getRootNode().get("persons");
        for (JsonNode personNode : personsNode) {
            if (personNode.get("lastName").asText().equals(lastName)) {
                Person person = buildPersonFromJsonNode(personNode);
                persons.add(person);
            }
        }
        return persons;
    }

    @Override
    public Set<Person> findByCity(String city) {
        Set<Person> persons = new HashSet<>();
        final JsonNode personsNode = jsonReader.getRootNode().get("persons");
        for (JsonNode personNode : personsNode) {
            if (personNode.get("city").asText().equals(city)) {
                Person person = buildPersonFromJsonNode(personNode);
                persons.add(person);
            }
        }
        return persons;
    }

    @Override
    public boolean save(Person person) {
        final JsonNode rootNode = jsonReader.getRootNode();
        ObjectNode newPersonNode = objectMapper.createObjectNode();
        newPersonNode.put("firstName", person.getFirstName());
        newPersonNode.put("lastName", person.getLastName());
        newPersonNode.put("address", person.getAddress());
        newPersonNode.put("city", person.getCity());
        newPersonNode.put("zip", person.getZip());
        newPersonNode.put("phone", person.getPhone());
        newPersonNode.put("email", person.getEmail());
        ArrayNode personsNode = (ArrayNode) rootNode.get("persons");
        personsNode.add(newPersonNode);
        return jsonWriter.writeJson(rootNode);
    }

    @Override
    public boolean delete(String firstName, String lastName) {
        boolean deleted = false;
        final JsonNode rootNode = jsonReader.getRootNode();
        Iterator<JsonNode> personsNode = rootNode.get("persons").elements();
        while (personsNode.hasNext()) {
            JsonNode personNode = personsNode.next();
            if (personNode.get("firstName").asText().equals(firstName) &&
                    personNode.get("lastName").asText().equals(lastName)) {
                personsNode.remove();
                deleted = true;
            }
        }
        jsonWriter.writeJson(rootNode);
        return deleted;
    }

    @Override
    public Person findByFirstNameAndLastName(String firstName, String lastName) {
        final JsonNode rootNode = jsonReader.getRootNode();
        for (JsonNode personNode : rootNode.get("persons")) {
            if (personNode.get("firstName").asText().equals(firstName) &&
                    personNode.get("lastName").asText().equals(lastName)) {
                return buildPersonFromJsonNode(personNode);}
            }
        return null;
    }


    @Override
    public boolean update(Person person) {
        final JsonNode rootNode = jsonReader.getRootNode();
        for (JsonNode personNode : rootNode.get("persons")) {
            if (personNode.get("firstName").asText().equals(person.getFirstName()) &&
                    personNode.get("lastName").asText().equals(person.getLastName())) {
                        if (person.getAddress() != null) {
                ((ObjectNode) personNode).put("address", person.getAddress());
                        }
                        if (person.getCity() != null) {
                ((ObjectNode) personNode).put("city", person.getCity());
                        }
                        if (person.getZip() != null) {
                ((ObjectNode) personNode).put("zip", person.getZip());
                        }
                        if (person.getPhone() != null) {
                ((ObjectNode) personNode).put("phone", person.getPhone());
                        }
                        if (person.getEmail() != null) {
                ((ObjectNode) personNode).put("email", person.getEmail());
                        }
                return jsonWriter.writeJson(rootNode);
            }
        }
        return false;
    }

}
