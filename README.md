# Atlas

* The world is composed of a grid of cells.
* Each cell has food and water.
* Each cell has an elevation.

## How the game works

### Habitat

A organism prefers to live in a biome based on their genes. Occupying cells within a organism's preferred biome consumes the least amount of energy.

For example, a desert dweller prefers not to exist within a forest biome.

### Movement

A organism can move from their current cell to a neighbouring cell. Movement consumes food and water.
It costs a organism more health to move to a cell of higher elevation. For example: (toElevation / fromElevation) + 1.

### Food & Water

Each organism must consume food & water in order to survive. As time passes in the
world a organism becomes hungry and/or thirsty and must seek out food & water. The
rate at which a organism must eat or drink is dependent on a organism's genes.

### Attacking

### Mating

An organsim can only mate with another organism if they are genetically compatible.

### Genes

* PreferredTemperature: the preferred temperature for a organism.
* PreferredMoisture: the preferred moisure for a organism.
* PreferredElevation: the preferred elevation for a organism.
* EatFrequency: how often a organism needs to eat.
* DrinkFrequency: how often a organism needs to drink.
* ReproduceFrequency: how often a organism can reproduce.

## Client

# Tick

    {
      "organisms": [{"id": "123", "health": 100}],
      "cells": [
        {
          "position": [0, 0],
          "food": 1,
          "water": 2,
          "organisms": [
            {"id": "123", "health": 100}
            {"id": "456", "health": 99}
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
      "direction": "NW"
    }

Eat:

    {
      "action": "eat"
    }

Drink:

    {
      "action": "drink"
    }

Attack:

    {
      "action": "attack",
      "organism_id": "123"
    }

Mate:

    {
      "action": "mate",
      "organism_id": "123"
    }
