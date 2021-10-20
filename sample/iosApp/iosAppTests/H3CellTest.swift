//
//  H3CellTest.swift
//  iosAppTests
//
//  Created by Steve Kim on 2021/07/14.
//  Copyright © 2021 orgName. All rights reserved.
//

import shared
import XCTest

class H3CellTest: XCTestCase {
    
    override func setUp() {
        
    }
    
    func testBoundary() {
        
        let h3Indexes = [
            "87283082bffffff",
            "872830870ffffff",
            "872830820ffffff",
            "87283082effffff",
        ]
        
        let polygons = H3.Companion().polygons(h3Indexes: h3Indexes)
        
        print("polygons", polygons)
        
        XCTAssertEqual(h3Indexes.count, polygons.count)
    }
}
