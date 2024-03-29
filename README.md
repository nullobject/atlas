# Atlas

* The world is composed of a grid of cells.
* Each cell has food and water.
* Each cell has an elevation.

A organism prefers to live in a biome based on their genes. Occupying cells within a organism's preferred biome consumes the least amount of energy.

For example, a desert dweller prefers not to exist within a forest biome.

## How the game works

### Spawning

A player can join a game at any time. When a player connects a number of
organisms are randomly spawned into the world for them.

### Movement

A organism can move from their current cell to a neighbouring cell. Movement consumes food and water.
It costs a organism more health to move to a cell of higher elevation. For example: (toElevation / fromElevation) + 1.

### Food & Water

Each organism must consume food & water in order to survive. As time passes in the
world a organism becomes hungry and/or thirsty and must seek out food & water. The
rate at which a organism must eat or drink is dependent on a organism's genes.

### Attacking

### Genes

* PreferredTemperature: the preferred temperature for a organism.
* PreferredMoisture: the preferred moisure for a organism.
* PreferredElevation: the preferred elevation for a organism.
* EatFrequency: how often a organism needs to eat.
* DrinkFrequency: how often a organism needs to drink.
* ReproduceFrequency: how often a organism can reproduce.

## Client

# Messages

* When the client connects a number of organisms are spawned.
* The client requests the world view.
* The server responds to the client with a world view.
* The server processes the intention.

# Tick

    {
      "cells": [
        {
          "position": [0, 0],
          "food": 1,
          "water": 2,
          "organisms": [
            {"id": "123", "health": 100, "playerId": "abc"}
            {"id": "456", "health": 99, "playerId": "def"}
          ]
        }, {
          "position": [1, 0],
          "food": 3,
          "water": 4,
          "organisms": []
        }, {
          "position": [0, 1],
          "food": 5,
          "water": 6,
          "organisms": []
        }, {
          "position": [1, 1],
          "food": 7,
          "water": 8,
          "organisms": [
            {"id": "789", "health": 98}
          ]
        }
      ]
    }


# Action

Move:

    {
      "action": "move",
      "organismId": "123",
      "direction": "NW"
    }

Eat:

    {
      "action": "eat",
      "organismId": "123"
    }

Drink:

    {
      "action": "drink",
      "organismId": "123"
    }

Attack:

    {
      "action": "attack",
      "organismId": "123",
      "targetId": "456"
    }
