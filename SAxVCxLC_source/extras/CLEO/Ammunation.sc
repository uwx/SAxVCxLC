SCRIPT_START
{
    LVAR_FLOAT char_x char_y char_z sphere_x sphere_y sphere_z rotation x y z distance
    LVAR_INT sc_player sphere menu selected_item weapon_item weapon_price shopping_index index_no weapon_type player_money sc_char
    LVAR_TEXT_LABEL weapon_name sub_menu zone_name

    GET_PLAYER_CHAR 0 sc_player 

    ADD_TEXT_LABEL Header "Ammu-Nation"
    ADD_TEXT_LABEL Column1 "Pistols"
    ADD_TEXT_LABEL Column2 "Micro SMGs"
    ADD_TEXT_LABEL Column3 "Shotguns"
    ADD_TEXT_LABEL Column4 "Thrown"
    ADD_TEXT_LABEL Column5 "Armour"
    ADD_TEXT_LABEL Column6 "SMG"
    ADD_TEXT_LABEL Column7 "Rifle"
    ADD_TEXT_LABEL Column8 "Assult"
    ADD_TEXT_LABEL Pistol1 "9mm"
    ADD_TEXT_LABEL Pistol2 "Silenced 9mm"
    ADD_TEXT_LABEL Pistol3 "Desert Eagle"
    ADD_TEXT_LABEL SMG1 "Tec9"
    ADD_TEXT_LABEL SMG2 "MicroSMG"
    ADD_TEXT_LABEL Shotg1 "Shotgun"
    ADD_TEXT_LABEL Shotg2 "Sawnoff Shotgun"
    ADD_TEXT_LABEL Shotg3 "Combat Shotgun"
    ADD_TEXT_LABEL Thrown1 "Grenades"
    ADD_TEXT_LABEL Thrown2 "Remote Explosives"
    ADD_TEXT_LABEL Armour "Body Armour"
    ADD_TEXT_LABEL SMG "SMG"
    ADD_TEXT_LABEL Rifle1 "Rifle"
    ADD_TEXT_LABEL Rifle2 "Sniper Rifle"
    ADD_TEXT_LABEL Asslt1 "AK47"
    ADD_TEXT_LABEL Asslt2 "M4"

    REQUEST_MODEL WMYAMMO
    REQUEST_MODEL 348
    REQUEST_MODEL 372
    REQUEST_MODEL 352
    REQUEST_MODEL 351
    REQUEST_MODEL 342
    REQUEST_MODEL 373
    REQUEST_MODEL 353
    REQUEST_MODEL 350
    REQUEST_MODEL 347
    REQUEST_MODEL 363
    REQUEST_MODEL 357
    REQUEST_MODEL 358
    REQUEST_MODEL 355
    REQUEST_MODEL 356
    REQUEST_MODEL 348
    REQUEST_ANIMATION SHP_1H_RET_S
    REQUEST_ANIMATION SHP_TRAY_POSE
    LOAD_ALL_MODELS_NOW
    start:
    WHILE TRUE
        WAIT 0
        IF IS_CHAR_IN_ZONE sc_player WHOLEVC 
            WHILE IS_CHAR_IN_ZONE sc_player WHOLEVC
                WAIT 0
                GOSUB vc
            ENDWHILE
        ENDIF
        IF IS_CHAR_IN_ZONE sc_player WHOLELC 
            WHILE IS_CHAR_IN_ZONE sc_player WHOLElC
                WAIT 0
                GOSUB lc
            ENDWHILE
        ENDIF
    ENDWHILE
    lc:
    RETURN
    vc:
        zone_name = beach1
        char_x = 8186.1641
        char_y = -9734.4648
        char_z = 5.462
        rotation = 0.0
        sphere_x = 8186.2187
        sphere_y = -9731.4805
        sphere_z = 5.447
        GOSUB startup

        zone_name = beach3
        char_x = 8614.292
        char_y = -7196.9116
        char_z = 14.1701
        rotation = 14.1701
        sphere_x =  8614.3262
        sphere_y = -7194.3599
        sphere_z =  14.1736
        GOSUB startup

        zone_name = dtown
        char_x =  7570.019
        char_y = -7045.0933
        char_z =  6.0807
        rotation = 270.4       
        sphere_x = 7572.9248
        sphere_y = -7045.0518
        sphere_z = 6.0807
        GOSUB startup
    RETURN
    startup:

    IF IS_CHAR_IN_ZONE sc_player $zone_name 
        CREATE_CHAR PEDTYPE_DEALER WMYAMMO char_x char_y char_z sc_char
        SET_CHAR_HEADING sc_char rotation
        ADD_SPHERE sphere_x sphere_y sphere_z 1.0 sphere
        SET_CHAR_ONLY_DAMAGED_BY_PLAYER sc_char TRUE

        WHILE IS_CHAR_IN_ZONE sc_player $zone_name
            WAIT 0
            IF LOCATE_CHAR_ANY_MEANS_3D sc_player sphere_x sphere_y sphere_z 1.0 1.0 1.0 FALSE
                GET_CHAR_COORDINATES sc_char x y z
                GET_DISTANCE_BETWEEN_COORDS_3D char_x char_y char_z x y z distance
                IF NOT distance < 0.1
                    TASK_GO_STRAIGHT_TO_COORD sc_char char_x char_y char_z 6 5000
                    WAIT 5000
                    SET_CHAR_HEADING sc_char rotation
                ENDIF
                ATTACH_CAMERA_TO_CHAR_LOOK_AT_CHAR sc_player 0.0 0.0 1.0 sc_char 0.0 2
                GOSUB menu
            ENDIF
            GOSUB ped_reaction
            GOSUB check_ped 
        ENDWHILE

        REMOVE_CHAR_ELEGANTLY sc_char
        REMOVE_SPHERE sphere
    ENDIF
    RETURN

    menu:
    WAIT 0

    //Changes
    CLEAR_CHAR_TASKS_IMMEDIATELY sc_player
    SET_PLAYER_CONTROL 0 FALSE
    IF IS_PLAYER_CONTROL_ON 0
        GOTO menu
    ENDIF
    SET_CAMERA_IN_FRONT_OF_CHAR sc_char
    DISPLAY_RADAR FALSE
    SET_MINIGAME_IN_PROGRESS TRUE
    SET_CURRENT_CHAR_WEAPON sc_player WEAPONTYPE_ANYMELEE

    PRINT_HELP_FOREVER WARDH3 // Menu help text
    GOSUB main_menu
    WAIT 2000
    RETURN
    main_menu:

        CREATE_MENU Header 20.0 120.0 200.0 1 TRUE TRUE 1 (menu)
        SET_MENU_COLUMN menu 0 DUMMY Column1 Column2 Column3 Column4 Column5 Column6 Column7 Column8 DUMMY DUMMY DUMMY DUMMY
        WAIT 2000
        TASK_PLAY_ANIM sc_char SHP_TRAY_POSE WEAPONS 2.0 0 TRUE TRUE TRUE -1  
        WHILE NOT IS_KEY_PRESSED VK_RETURN
            WAIT 0
            GET_MENU_ITEM_SELECTED menu (selected_item)
            IF IS_KEY_PRESSED VK_SPACE  
                WAIT 500
                IF IS_INT_LVAR_EQUAL_TO_NUMBER selected_item 0
                    PISTOL:
                    DELETE_MENU menu
                    CREATE_MENU Header 20.0 120.0 200.0 1 TRUE TRUE 1 (menu) 
                    SET_MENU_COLUMN menu 0 DUMMY Pistol1 Pistol2 Pistol3 DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY
                    REMOVE_ALL_CHAR_WEAPONS sc_char

                    TASK_PLAY_ANIM sc_char SHP_TRAY_POSE WEAPONS 2.0 0 TRUE TRUE TRUE -1 
                    WHILE NOT IS_KEY_PRESSED VK_RETURN

                        shopping_index = 0
                        index_no = 0
                        weapon_type = 22
                        GOSUB CORE

                        shopping_index = 8
                        index_no = 1
                        weapon_type = 23
                        GOSUB CORE

                        shopping_index = 14
                        index_no = 2
                        weapon_type = 24
                        GOSUB CORE

                    ENDWHILE
                    CLEAR_CHAR_TASKS_IMMEDIATELY sc_char
                    
                ENDIF

                IF IS_INT_LVAR_EQUAL_TO_NUMBER selected_item 1
                    SMG:
                    DELETE_MENU menu
                    CREATE_MENU Header 20.0 120.0 200.0 1 TRUE TRUE 1 (menu)
                    SET_MENU_COLUMN menu 0 DUMMY SMG1 SMG2 DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY
                    REMOVE_ALL_CHAR_WEAPONS sc_char

                    TASK_PLAY_ANIM sc_char SHP_TRAY_POSE WEAPONS 2.0 0 TRUE TRUE TRUE -1 
                    WHILE NOT IS_KEY_PRESSED VK_RETURN
                        shopping_index = 1
                        index_no = 0
                        weapon_type = 32
                        GOSUB CORE

                        shopping_index = 2
                        index_no = 1
                        weapon_type = 28
                        GOSUB CORE

                    ENDWHILE
                    CLEAR_CHAR_TASKS_IMMEDIATELY sc_char

                ENDIF
                IF IS_INT_LVAR_EQUAL_TO_NUMBER selected_item 2
                    SHOTG:
                    DELETE_MENU menu
                    CREATE_MENU Header 20.0 120.0 200.0 1 TRUE TRUE 1 (menu)
                    SET_MENU_COLUMN menu 0 DUMMY Shotg1 Shotg2 Shotg3 DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY
                    REMOVE_ALL_CHAR_WEAPONS sc_char

                    TASK_PLAY_ANIM sc_char SHP_TRAY_POSE WEAPONS 2.0 0 TRUE TRUE TRUE -1 
                    WHILE NOT IS_KEY_PRESSED VK_RETURN

                        shopping_index = 3
                        index_no = 0
                        weapon_type = 25
                        GOSUB CORE

                        shopping_index = 7
                        index_no = 1
                        weapon_type = 26
                        GOSUB CORE

                        shopping_index = 15
                        index_no = 2
                        weapon_type = 27
                        GOSUB CORE

                    ENDWHILE
                    CLEAR_CHAR_TASKS_IMMEDIATELY sc_char
                ENDIF
                IF IS_INT_LVAR_EQUAL_TO_NUMBER selected_item 3
                    THROWN:
                    DELETE_MENU menu
                    CREATE_MENU Header 20.0 120.0 200.0 1 TRUE TRUE 1 (menu)
                    SET_MENU_COLUMN menu 0 DUMMY Thrown1 Thrown2 DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY
                    REMOVE_ALL_CHAR_WEAPONS sc_char

                    TASK_PLAY_ANIM sc_char SHP_TRAY_POSE WEAPONS 2.0 0 TRUE TRUE TRUE -1 
                    WHILE NOT IS_KEY_PRESSED VK_RETURN

                        shopping_index = 4
                        index_no = 0
                        weapon_type = 16
                        GOSUB CORE

                        shopping_index = 9
                        index_no = 1
                        weapon_type = 39
                        GOSUB CORE

                    ENDWHILE
                    CLEAR_CHAR_TASKS_IMMEDIATELY sc_char
                ENDIF
                IF IS_INT_LVAR_EQUAL_TO_NUMBER selected_item 4

                    ARMOUR:

                    DELETE_MENU menu
                    CREATE_MENU Header 20.0 120.0 200.0 1 TRUE TRUE 1 (menu)
                    SET_MENU_COLUMN menu 0 DUMMY Armour DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY
                    REMOVE_ALL_CHAR_WEAPONS sc_char

                    TASK_PLAY_ANIM sc_char SHP_TRAY_POSE WEAPONS 2.0 0 TRUE TRUE TRUE -1 
                    WHILE NOT IS_KEY_PRESSED VK_RETURN

                        shopping_index = 5
                        index_no = 0
                        weapon_type = 48
                        GOSUB CORE

                    ENDWHILE
                    CLEAR_CHAR_TASKS_IMMEDIATELY sc_char
                ENDIF
                IF IS_INT_LVAR_EQUAL_TO_NUMBER selected_item 5
                    MP5:
                    DELETE_MENU menu
                    CREATE_MENU Header 20.0 120.0 200.0 1 TRUE TRUE 1 (menu)
                    SET_MENU_COLUMN menu 0 DUMMY SMG DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY
                    REMOVE_ALL_CHAR_WEAPONS sc_char

                    TASK_PLAY_ANIM sc_char SHP_TRAY_POSE WEAPONS 2.0 0 TRUE TRUE TRUE -1 
                    WHILE NOT IS_KEY_PRESSED VK_RETURN

                        shopping_index = 6
                        index_no = 0
                        weapon_type = 29
                        GOSUB CORE

                    ENDWHILE
                    CLEAR_CHAR_TASKS_IMMEDIATELY sc_char

                ENDIF
                IF IS_INT_LVAR_EQUAL_TO_NUMBER selected_item 6
                    RIFLE:
                    DELETE_MENU menu
                    CREATE_MENU Header 20.0 120.0 200.0 1 TRUE TRUE 1 (menu)
                    SET_MENU_COLUMN menu 0 DUMMY Rifle1 Rifle2 DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY
                    REMOVE_ALL_CHAR_WEAPONS sc_char

                    TASK_PLAY_ANIM sc_char SHP_TRAY_POSE WEAPONS 2.0 0 TRUE TRUE TRUE -1 
                    WHILE NOT IS_KEY_PRESSED VK_RETURN

                        shopping_index = 11
                        index_no = 0
                        weapon_type = 34
                        GOSUB CORE

                        shopping_index = 10
                        index_no = 1
                        weapon_type = 33
                        GOSUB CORE

                    ENDWHILE
                    CLEAR_CHAR_TASKS_IMMEDIATELY sc_char
                ENDIF
                IF IS_INT_LVAR_EQUAL_TO_NUMBER selected_item 7
                    ASSULT:
                    DELETE_MENU menu
                    CREATE_MENU Header 20.0 120.0 200.0 1 TRUE TRUE 1 (menu)
                    SET_MENU_COLUMN menu 0 DUMMY Asslt1 Asslt2 DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY
                    REMOVE_ALL_CHAR_WEAPONS sc_char

                    TASK_PLAY_ANIM sc_char SHP_TRAY_POSE WEAPONS 2.0 0 TRUE TRUE TRUE -1                                         
                    WHILE NOT IS_KEY_PRESSED VK_RETURN

                        shopping_index = 12
                        index_no = 0
                        weapon_type = 30
                        GOSUB CORE

                        shopping_index = 13
                        index_no = 1
                        weapon_type = 31
                        GOSUB CORE

                    ENDWHILE
                    CLEAR_CHAR_TASKS_IMMEDIATELY sc_char
                ENDIF
            ENDIF
            IF IS_KEY_PRESSED VK_RETURN
                DELETE_MENU menu
                GOTO main_menu
            ENDIF

        ENDWHILE

        CLEAR_HELP
        DELETE_MENU menu
        SET_PLAYER_CONTROL 0 TRUE
        SET_CAMERA_ZOOM 0
        DISPLAY_RADAR TRUE
        SET_MINIGAME_IN_PROGRESS TRUE
        CLEAR_CHAR_TASKS sc_char
        RESTORE_CAMERA
        WAIT 1000
    RETURN
    CORE:
        WAIT 0
        GET_MENU_ITEM_SELECTED menu (selected_item)
        
        IF IS_INT_LVAR_EQUAL_TO_INT_LVAR selected_item index_no
        AND IS_KEY_PRESSED VK_SPACE 
            WAIT 1000

            GIVE_WEAPON_TO_CHAR sc_char weapon_type 9999
            TASK_PLAY_ANIM sc_char SHP_1H_RET_S WEAPONS 2.0 0 TRUE TRUE TRUE -1
            LOAD_SHOP "ammun1"
            GET_ITEM_IN_SHOP shopping_index weapon_item 
            GET_PRICE_OF_ITEM weapon_item weapon_price
            GET_NAME_OF_ITEM weapon_item weapon_name

            //Setting up the menu
            DELETE_MENU menu
            CREATE_MENU Header 20.0 120.0 200.0 2 TRUE TRUE 1 (menu) 
            SET_MENU_COLUMN_ORIENTATION menu 0 1
            SET_MENU_COLUMN menu 0 WEAPON $weapon_name DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY
            SET_MENU_COLUMN_ORIENTATION menu 1 0
            SET_MENU_COLUMN menu 1 COST DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY
            SET_MENU_COLUMN_WIDTH menu 0 140
            SET_MENU_COLUMN_WIDTH menu 1 46
            SET_MENU_ITEM_WITH_NUMBER menu 1 0 DOLLAR weapon_price


            WHILE NOT IS_KEY_PRESSED VK_RETURN
                WAIT 0
                GET_MENU_ITEM_SELECTED menu (selected_item)
                READ_MEMORY 0xB7CE50 4 FALSE player_money
                IF IS_INT_LVAR_GREATER_OR_EQUAL_TO_INT_LVAR player_money weapon_price
                    PRINT_NOW AMMUA 1000 TRUE 
                ENDIF
                

                
                IF IS_INT_LVAR_EQUAL_TO_NUMBER selected_item 0
                AND IS_KEY_PRESSED VK_SPACE
                    WAIT 1000
                    IF IS_INT_LVAR_GREATER_OR_EQUAL_TO_INT_LVAR player_money weapon_price
                        BUY_ITEM weapon_item 
                    ELSE
                        PRINT_NOW SHOPNO 1000 TRUE
                    ENDIF
                ENDIF
                IF IS_KEY_PRESSED VK_RETURN
                    WAIT 1000
                    IF IS_INT_LVAR_EQUAL_TO_NUMBER weapon_type 22
                    OR IS_INT_LVAR_EQUAL_TO_NUMBER weapon_type 23
                    OR IS_INT_LVAR_EQUAL_TO_NUMBER weapon_type 24
                        GOTO PISTOL
                    ENDIF
                    
                    IF IS_INT_LVAR_EQUAL_TO_NUMBER weapon_type 28
                    OR IS_INT_LVAR_EQUAL_TO_NUMBER weapon_type 32
                        GOTO SMG
                    ENDIF

                    IF IS_INT_LVAR_EQUAL_TO_NUMBER weapon_type 25
                    OR IS_INT_LVAR_EQUAL_TO_NUMBER weapon_type 26
                    OR IS_INT_LVAR_EQUAL_TO_NUMBER weapon_type 27
                        GOTO SHOTG
                    ENDIF

                    IF IS_INT_LVAR_EQUAL_TO_NUMBER weapon_type 16
                    OR IS_INT_LVAR_EQUAL_TO_NUMBER weapon_type 39
                        GOTO THROWN
                    ENDIF

                    IF IS_INT_LVAR_EQUAL_TO_NUMBER weapon_type 48
                        GOTO ARMOUR
                    ENDIF

                    IF IS_INT_LVAR_EQUAL_TO_NUMBER weapon_type 29
                        GOTO MP5
                    ENDIF

                    IF IS_INT_LVAR_EQUAL_TO_NUMBER weapon_type 33
                    OR IS_INT_LVAR_EQUAL_TO_NUMBER weapon_type 34
                        GOTO RIFLE
                    ENDIF

                    IF IS_INT_LVAR_EQUAL_TO_NUMBER weapon_type 30
                    OR IS_INT_LVAR_EQUAL_TO_NUMBER weapon_type 31
                        GOTO ASSULT
                    ENDIF
                ENDIF
            ENDWHILE
        ENDIF
    RETURN

    ped_reaction:
        IF IS_CHAR_SHOOTING sc_player
        OR IS_PLAYER_TARGETTING_CHAR 0 sc_char
        OR HAS_CHAR_BEEN_DAMAGED_BY_WEAPON sc_char WEAPONTYPE_ANYMELEE
            TASK_STAND_STILL sc_char 5000
            SET_CHAR_WEAPON_SKILL sc_char 1
            GIVE_WEAPON_TO_CHAR sc_char WEAPONTYPE_PISTOL 99999
            SET_CHAR_ACCURACY sc_char 80
            REMOVE_SPHERE sphere
            WHILE NOT IS_CHAR_DEAD sc_player
            OR NOT IS_CHAR_DEAD sc_char
            OR NOT IS_CHAR_IN_ZONE sc_player $zone_name
                WAIT 0
                TASK_KILL_CHAR_ON_FOOT sc_char sc_player
            ENDWHILE
            WAIT 5000
            DELETE_CHAR sc_char
            GOTO start
        ENDIF
    RETURN

    check_ped:
        IF IS_CHAR_DEAD sc_char
            ALTER_WANTED_LEVEL 0 3
            REMOVE_SPHERE sphere
            WHILE NOT IS_CHAR_DEAD sc_player
            OR NOT IS_CHAR_IN_ZONE sc_player $zone_name
                WAIT 0
            ENDWHILE
            DELETE_CHAR sc_char
            GOTO start
        ENDIF
    RETURN

}
SCRIPT_END