# MC-LootChest

A Minecraft loot chest, that gives the player a random item when opened.

When the player joins the server, a loot chest is added to the inventory. By interacting with
it an inventory with the size 9 x 3 opens:

```
x  x  x  x  o  x  x  x  x
s  s  s  s  w  s  s  s  s
x  x  x  x  o  x  x  x  x
<------------------------
```

Where `x` is a purple and `o` is an orange stained glass pane.
`s` and `w` are items that are moving from the right to left side of the inventory box.
After each step a new item gets added to the right end of the line.
When 113 steps have been passed, the item on position w is won
and will be added to the inventory of the player.