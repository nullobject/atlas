# Atlas

* The world is composed of a grid of cells.
* Each cell has food and water.
* Each cell has an elevation.

## How the game works

### Movement

A player can move from their current cell to a neighbouring cell. Movement consumes food and water.
It costs a player more health to move to a cell of higher elevation. For example: (toElevation / fromElevation) + 1.

### Food & Water

Each player must consume food & water in order to survive. As time passes in the
world a player becomes hungry and/or thirsty and must seek out food & water. The
rate at which a player must eat or drink is dependent on a player's genes.

### Attacking

### Reproduction

### Genes

* EatFrequency: how often a player needs to eat.
* DrinkFrequency: how often a player needs to drink.
* ReproduceFrequency: how often a player can reproduce.

## Client

# Tick

    {
      "player": {"id": "123", "health": 100},
      "world": {
        "cells": [
          {
            "position": [0, 0],
            "food": 1,
            "water": 2,
            "players": [
              {"id": "123", "health": 100}
              {"id": "456", "health": 99}
            ]
          }, {
            "position": [1, 0],
            "food": 3,
            "water": 4,
            "players": []
          }, {
            "position": [0, 1],
            "food": 5,
            "water": 6,
            "players": []
          }, {
            "position": [1, 1],
            "food": 7,
            "water": 8,
            "players": [
              {"id": "789", "health": 98}
            ]
          }
        ]
      }
    }


# Action

Move the bot in the given direction:

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
