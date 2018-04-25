format 218

classcanvas 128010 class_ref 128010 // Population
  classdiagramsettings member_max_width 0 end
  xyzwh 124 76 2000 149 83
end
classcanvas 128138 class_ref 134538 // Individual
  classdiagramsettings member_max_width 0 end
  xyzwh 50 220 2000 299 95
end
classcanvas 128522 class_ref 134922 // SpecialZone
  classdiagramsettings member_max_width 0 show_parameter_dir no end
  xyzwh 624 453 2000 133 95
end
classcanvas 128650 class_ref 135050 // Map
  classdiagramsettings member_max_width 0 end
  xyzwh 550 653 2000 281 83
end
packagecanvas 130442 
  package_ref 134538 // Point
   color lightblue  xyzwh 30 453 2000 273 173
end
classcanvas 130570 class_ref 134666 // Point
  classdiagramsettings member_max_width 0 end
  xyzwh 129 502 2005 137 103
end
classcanvas 131082 class_ref 134794 // Obstacle
  classdiagramsettings member_max_width 0 end
  xyzwh 367 670 2005 133 59
end
packagecanvas 131722 
  package_ref 128010 // Environment
   color lightblue  xyzwh 363 398 0 481 363
end
packagecanvas 133002 
  package_ref 134666 // Agents
   color lightblue  xyzwh 36 26 0 345 377
end
classcanvas 133130 class_ref 135178 // Path
  classdiagramsettings member_max_width 0 end
  xyzwh 46 332 2005 115 65
end
relationcanvas 128778 relation_ref 128010 // Pop
  from ref 128010 z 2001 label "Pop" italic max_width 255 xyz 203 182.5 3000 to ref 128138
  no_role_a no_role_b
  multiplicity_a_pos 184 206 3000 no_multiplicity_b
end
relationcanvas 129034 relation_ref 128266 // SpZones
  from ref 128650 z 2001 label "SpZones" italic max_width 255 xyz 692 592.5 3000 to ref 128522
  no_role_a no_role_b
  multiplicity_a_pos 676 552 3000 no_multiplicity_b
end
relationcanvas 130826 relation_ref 129418 // 
  from ref 128138 z 2006 to ref 130570
  no_role_a role_b_pos 208 319 3000
  multiplicity_a_pos 183 488 3000 no_multiplicity_b
end
relationcanvas 130954 relation_ref 129546 // Area
  geometry HVH
  from ref 128522 z 2006 to point 442 497
  line 132746 z 2006 to point 442 551
  line 132874 z 2006 label "Area" italic max_width 255 xyz 331 543 3000 to ref 130570
  role_a_pos 275 540 3000 no_role_b
  no_multiplicity_a no_multiplicity_b
end
relationcanvas 131210 relation_ref 129674 // <generalisation>
  geometry VHV unfixed
  from ref 131082 z 2006 to point 430 645
  line 132234 z 2006 to point 192 645
  line 132362 z 2006 to ref 130570
  no_role_a no_role_b
  no_multiplicity_a no_multiplicity_b
end
relationcanvas 131338 relation_ref 129802 // ObstList
  from ref 128650 z 2006 label "ObstList" italic max_width 255 xyz 505.5 686 3000 to ref 131082
  no_role_a no_role_b
  multiplicity_a_pos 509 701 3000 no_multiplicity_b
end
relationcanvas 131594 relation_ref 130058 // initFinal
  geometry HVH unfixed
  decenter_end 786
  from ref 128650 z 2006 to point 536 679
  line 132490 z 2006 to point 536 580
  line 132618 z 2006 label "initFinal" italic max_width 255 xyz 384.5 572 3000 to ref 130570
  no_role_a no_role_b
  multiplicity_a_pos 275 587 3000 no_multiplicity_b
end
line 133386 -_-_ decenter_begin 461 decenter_end 260
  from ref 133130 z 2007 to ref 130826
end
